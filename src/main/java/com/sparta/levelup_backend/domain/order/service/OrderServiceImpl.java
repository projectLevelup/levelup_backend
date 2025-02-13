package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.exception.common.OrderException;
import com.sparta.levelup_backend.utill.OrderStatus;
import com.sparta.levelup_backend.utill.ProductStatus;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductServiceImpl productServiceImpl;

    /**
     * 주문생성
     * @param dto productId
     * @return orderId, productId, productName, status, price
     */
    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto) {

        UserEntity user = userRepository.findByIdOrElseThrow(userId);

        ProductEntity product = productServiceImpl.findById(dto.getProductId());

        if (product.getStatus().equals(ProductStatus.INACTIVE)) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 재고 차감 메소드 호출
        productServiceImpl.decreaseAmount(dto.getProductId());

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalPrice(product.getPrice())
                .product(product)
                .build();

        OrderEntity saveOrder = orderRepository.save(order);

        return new OrderResponseDto(saveOrder);
    }

    /**
     * 주문 조회
     * @param orderId 조회 주문 id
     * @return orderId, productId, productName, status, price
     */
    @Override
    public OrderResponseDto findOrder(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 구매자와 판매자만 조회 가능
        if (!order.getUser().getId().equals(userId) && !order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return new OrderResponseDto(order);
    }

    /**
     * 주문 상태 변경
     * @param orderId 변경할 주문 id
     * @return orderId, productId, productName, status, price
     */
    @Override
    public OrderResponseDto updateOrder(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 판매자인지 확인
        if (!order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 결제 대기 상태가 아니라면 변경 불가
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(OrderStatus.TRADING);
        orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    /**
     * 거래 완료
     * @param userId 사용자 id
     * @param orderId 변경할 주문 id
     * @return orderId, productId, productName, status, price
     */
    @Override
    public OrderResponseDto completeOrder(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 구매자인지 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 거래중 상태가 아니라면 변경 불가
        if (order.getStatus() != OrderStatus.TRADING) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    /**
     * 주문 취소(결제 요청 상태일때)
     * @param userId 구매자 id Or 판매자 id
     * @param orderId 주문 id
     * @return null
     */
    @Transactional
    @Override
    public void deleteOrderByPending(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 판매자 구매자 둘 다 취소 가능
        if (!order.getUser().getId().equals(userId) && !order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 거래요청이 아닐 때 예외 발생
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // 재고 복구 메소드 호출
        productServiceImpl.increaseAmount(order.getProduct().getId());

        order.setStatus(OrderStatus.CANCELED);
        order.orderDelete();
        orderRepository.save(order);
    }

    /**
     * 결제 취소(거래중 일 때 판매자만 가능)
     * @param userId 판매자 id
     * @param orderId 주문 id
     * @return null
     */
    @Transactional
    @Override
    public void deleteOrderByTrading(Long userId, Long orderId) {

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        // 판매자인지 확인
        if (!order.getProduct().getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 거래중이 아닐 때 예외 발생
        if (order.getStatus() != OrderStatus.TRADING) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // 재고 복구 메소드 호출
        productServiceImpl.increaseAmount(order.getProduct().getId());

        order.setStatus(OrderStatus.CANCELED);
        order.orderDelete();
        orderRepository.save(order);
    }
}