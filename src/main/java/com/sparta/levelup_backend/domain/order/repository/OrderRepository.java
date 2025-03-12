package com.sparta.levelup_backend.domain.order.repository;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.enums.OrderStatus;
import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.order.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, OrderStatus status);

    default OrderEntity findByIdOrElseThrow(Long orderId) {
        return findById(orderId).orElseThrow(() -> new OrderException(ORDER_NOT_FOUND));
    }

    // 학생의 주문 목록
    List<OrderEntity> findAllByUserId(Long userId);

    // 튜터 주문 목록
    @Query("SELECT o FROM OrderEntity o WHERE o.product.user.id = :tutorId")
    List<OrderEntity> findAllByTutorId(Long tutorId);
}
