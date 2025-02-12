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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public OrderResponseDto orderCreate(OrderCreateRequestDto dto) {
        Long userId = 2L;

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
    public OrderResponseDto findOrder(Long orderId) {
        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        return new OrderResponseDto(order);
    }

    /**
     * 주문 상태 변경
     * @param orderId 변경할 주문 id
     * @param status TRADING, COMPLETED
     * @return orderId, productId, productName, status, price
     */
    @Override
    public OrderResponseDto orderUpdate(Long orderId, OrderStatus status) {
        Long userId = 2L;

        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        if (order.getIsDeleted()) {
            throw new OrderException(ErrorCode.DUPLICATE_CANCELED_ORDER);
        }

        OrderStatus orderStatus = order.getStatus();

        // 결제 취소 불가능
        if (status == OrderStatus.CANCELED) {
            throw new OrderException(ErrorCode.INVALID_ORDER_CANCELED);
        }

        // 결제요청 -> 결제완료 변경 불가능
        if (orderStatus == OrderStatus.PENDING && status == OrderStatus.COMPLETED) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // 거래중 -> 결제요청 변경 불가능
        if (orderStatus == OrderStatus.TRADING && status == OrderStatus.PENDING) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }


        order.setStatus(status);
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }
}
