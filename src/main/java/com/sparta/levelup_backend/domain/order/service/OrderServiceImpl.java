package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.service.UserServiceImpl;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.OrderException;
import com.sparta.levelup_backend.utill.OrderStatus;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productServiceImpl;

    /**
     * 주문생성
     * @param dto productId
     * @return orderId, productId, productName, status, price
     */
    @Override
    @Transactional
    public OrderResponseDto orderCreate(Long userId, OrderCreateRequestDto dto) {

        UserEntity user = userService.findById(userId);

        ProductEntity product = productServiceImpl.findById(dto.getProductId());

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
    public OrderResponseDto orderUpdate(Long userId, Long orderId) {

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
    public OrderResponseDto orderComplete(Long userId, Long orderId) {

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
}