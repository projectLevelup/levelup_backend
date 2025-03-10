package com.sparta.levelup_backend.domain.order.repository;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.enums.OrderStatus;
import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.order.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, OrderStatus status);

    default OrderEntity findByIdOrElseThrow(Long orderId) {
        return findById(orderId).orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
    }
}
