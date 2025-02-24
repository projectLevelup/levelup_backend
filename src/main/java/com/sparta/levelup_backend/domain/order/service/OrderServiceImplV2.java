package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import com.sparta.levelup_backend.domain.bill.service.BillServiceImplV2;
import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.*;
import com.sparta.levelup_backend.utill.OrderStatus;
import com.sparta.levelup_backend.utill.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;
import static com.sparta.levelup_backend.utill.OrderStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImplV2 implements OrderServiceV2 {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductServiceImpl productService;
    private final RedissonClient redissonClient;
    private final BillServiceImplV2 billService;
    private final BillRepository billRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 주문생성
     *
     * @param dto productId
     * @return orderId, productId, productName, status, price
     */
    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto) {

        RLock lock = redissonClient.getLock("stock_lock_" + dto.getProductId());

        UserEntity user = userRepository.findByIdOrElseThrow(userId);

        OrderEntity saveOrder = null;

        try {
            boolean avaiable = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!avaiable) {
                throw new LockException(CONFLICT_LOCK_GET);
            }

            // 비관적 락
            ProductEntity product = productService.getFindByIdWithLock(dto.getProductId());

            if (product.getStatus().equals(ProductStatus.INACTIVE)) {
                throw new NotFoundException(PRODUCT_NOT_FOUND);
            }

            if (user.getId() == product.getUser().getId()) {
                throw new OrderException(INVALID_ORDER_CREATE);
            }

            product.decreaseAmount();

            OrderEntity order = OrderEntity.builder()
                    .user(user)
                    .status(PENDING)
                    .totalPrice(product.getPrice())
                    .product(product)
                    .orderName(product.getProductName())
                    .build();


            saveOrder = orderRepository.save(order);

            // Redis에 주문 Id 저장 (10분 TTL)
            redisTemplate.opsForValue().set("order:expire:" + order.getId(), "PENDING", Duration.ofMinutes(10));
        } catch (InterruptedException e) {
            throw new LockException(CONFLICT_LOCK_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return new OrderResponseDto(saveOrder);
    }

    /**
     * 주문 조회
     *
     * @param orderId 조회 주문 id
     * @return orderId, productId, productName, status, price
     */
    @Override
    public OrderResponseDto findOrder(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 구매자와 판매자만 조회 가능
        if (!order.getUser().getId().equals(userId) && !order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(FORBIDDEN_ACCESS);
        }

        return new OrderResponseDto(order);
    }

    /**
     * 주문 상태 변경
     *
     * @param orderId 변경할 주문 id
     * @return orderId, productId, productName, status, price
     */
    @Override
    @Transactional
    public OrderResponseDto updateOrder(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 구매자인지 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException(FORBIDDEN_ACCESS);
        }

        // 결제 대기 상태가 아니라면 변경 불가
        if (order.getStatus() != PENDING) {
            throw new OrderException(INVALID_ORDER_STATUS);
        }

        order.setStatus(TRADING);
        OrderEntity saveOrder = orderRepository.save(order);
        billService.createBill(userId, orderId);
        return new OrderResponseDto(saveOrder);
    }

    /**
     * 거래 완료
     *
     * @param userId  사용자 id
     * @param orderId 변경할 주문 id
     * @return orderId, productId, productName, status, price
     */
    @Override
    public OrderResponseDto completeOrder(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 구매자인지 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException(FORBIDDEN_ACCESS);
        }

        // 거래중 상태가 아니라면 변경 불가
        if (order.getStatus() != TRADING) {
            throw new OrderException(INVALID_ORDER_STATUS);
        }

        order.setStatus(COMPLETED);
        orderRepository.save(order);
        log.info("order {} 거래 완료.", order.getId());
        return new OrderResponseDto(order);
    }

    /**
     * 주문 취소(결제 요청 상태일때)
     *
     * @param userId  구매자 id Or 판매자 id
     * @param orderId 주문 id
     * @return null
     */
    @Transactional
    @Override
    public void deleteOrderByPending(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        RLock lock = redissonClient.getLock("stock_lock_" + order.getProduct().getId());

        // 판매자 구매자 둘 다 취소 가능
        if (!order.getUser().getId().equals(userId) && !order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(FORBIDDEN_ACCESS);
        }

        // 거래요청이 아닐 때 예외 발생
        if (order.getStatus() != PENDING) {
            throw new OrderException(INVALID_ORDER_STATUS);
        }

        try {
            boolean avaiable = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!avaiable) {
                throw new LockException(CONFLICT_LOCK_GET);
            }
            ProductEntity product = productService.getFindByIdWithLock(order.getProduct().getId());
            product.increaseAmount();
            order.setStatus(CANCELED);
            order.orderDelete();
            orderRepository.save(order);
        } catch (InterruptedException e) {
            throw new LockException(CONFLICT_LOCK_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    /**
     * 결제 취소(거래중 일 때 판매자만 가능)
     *
     * @param userId  판매자 id
     * @param orderId 주문 id
     * @return null
     */
    @Transactional
    @Override
    public void deleteOrderByTrading(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        RLock lock = redissonClient.getLock("stock_lock_" + order.getProduct().getId());

        // 판매자인지 확인
        if (!order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(FORBIDDEN_ACCESS);
        }

        // 거래중이 아닐 때 예외 발생
        if (order.getStatus() != TRADING) {
            throw new OrderException(INVALID_ORDER_STATUS);
        }

        try {
            boolean avaiable = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!avaiable) {
                throw new LockException(CONFLICT_LOCK_GET);
            }
            ProductEntity product = productService.getFindByIdWithLock(order.getProduct().getId());
            product.increaseAmount();
            order.setStatus(CANCELED);
            order.orderDelete();

            BillEntity bill = billRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new NotFoundException(BILL_NOT_FOUND));

            bill.cancelBill();

            orderRepository.save(order);
            billRepository.save(bill);

        } catch (InterruptedException e) {
            throw new LockException(CONFLICT_LOCK_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
