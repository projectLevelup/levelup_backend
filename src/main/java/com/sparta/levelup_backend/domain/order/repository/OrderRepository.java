package com.sparta.levelup_backend.domain.order.repository;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.utill.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, OrderStatus status);

}
