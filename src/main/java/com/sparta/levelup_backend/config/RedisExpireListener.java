package com.sparta.levelup_backend.config;

import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import com.sparta.levelup_backend.domain.bill.service.BillServiceImplV2;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.LockException;
import com.sparta.levelup_backend.utill.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.sparta.levelup_backend.exception.common.ErrorCode.CONFLICT_LOCK_ERROR;
import static com.sparta.levelup_backend.exception.common.ErrorCode.CONFLICT_LOCK_GET;
import static com.sparta.levelup_backend.utill.OrderStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisExpireListener implements MessageListener {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final RedissonClient redissonClient;

    @Transactional
    public void handleOrderExpiration(Long orderId) {
        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);
        RLock lock = redissonClient.getLock("stock_lock_" + order.getProduct().getId());

        if (order.getStatus() == PENDING) {

            try {
                boolean avaiable = lock.tryLock(1, 10, TimeUnit.SECONDS);
                if (!avaiable) {
                    throw new LockException(CONFLICT_LOCK_GET);
                }
                ProductEntity product = productService.getFindByIdWithLock(order.getProduct().getId());
                product.increaseAmount();
                productRepository.save(product);
                log.info("상품: {} 수량 복구 완료", product.getProductName());
                orderRepository.delete(order);
            } catch (InterruptedException e) {
                throw new LockException(CONFLICT_LOCK_ERROR);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

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
