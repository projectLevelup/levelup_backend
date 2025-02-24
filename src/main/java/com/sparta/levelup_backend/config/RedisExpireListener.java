package com.sparta.levelup_backend.config;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.utill.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.levelup_backend.utill.OrderStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisExpireListener implements MessageListener {

    private final OrderRepository orderRepository;

    @Transactional
    public void handleOrderExpiration(Long orderId) {
        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        if (order.getStatus() == PENDING) {
            orderRepository.delete(order);
            log.info("Order {}가 만료로 인해 삭제되었습니다.", orderId);
        }
    }

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
            String expiredKey = message.toString();

            if (expiredKey.startsWith("order:expire:")) {
                Long orderId = Long.parseLong(expiredKey.replace("order:expire:", ""));
                handleOrderExpiration(orderId);
            }
        }
    }
