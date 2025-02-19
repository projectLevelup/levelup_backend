package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.ProductStatus;
import com.sparta.levelup_backend.utill.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private BillRepository billRepository;

    @AfterEach
    void cleanup() {
        billRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UserEntity createUser(String email) {
        UserEntity user = UserEntity.builder()
                .email(email)
                .password("password")
                .nickName("Test User")
                .role(UserRole.USER)
                .phoneNumber("01012345678")
                .build();
        return userRepository.save(user);
    }

    private GameEntity createGame(UserEntity user) {
        GameEntity game = GameEntity.builder()
                .name("Test Game")
                .imgUrl("test.jpg")
                .genre("Action")
                .user(user)
                .build();
        return gameRepository.save(game);
    }

    private ProductEntity createProduct(UserEntity seller, GameEntity game, int amount) {
        ProductEntity product = ProductEntity.builder()
                .user(seller)
                .game(game)
                .productName("Test Product")
                .contents("Test Contents")
                .price(10000L)
                .amount(amount)
                .status(ProductStatus.ACTIVE)
                .imgUrl("test.jpg")
                .build();
        return productRepository.save(product);
    }

    @Test
    @DisplayName("동시에 여러 주문이 들어올 때 재고 감소가 정상적으로 동작하는지 테스트")
    void concurrentOrderTest() throws InterruptedException {

        // Given
        int threadCount = 10;
        int initialAmount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Test data setup with unique emails
        UserEntity seller = createUser("seller_" + UUID.randomUUID() + "@test.com");
        UserEntity buyer = createUser("buyer_" + UUID.randomUUID() + "@test.com");
        GameEntity game = createGame(seller);
        ProductEntity product = createProduct(seller, game, initialAmount);

        // When
        List<Future<OrderResponseDto>> futures = new ArrayList<>();

        // 모든 스레드를 준비
        for (int i = 0; i < threadCount; i++) {
            Future<OrderResponseDto> future = executorService.submit(() -> {
                try {
                    return orderService.createOrder(
                            buyer.getId(),
                            new OrderCreateRequestDto(product.getId())
                    );
                } catch (Exception e) {
                    throw e;
                } finally {
                    latch.countDown();
                }
            });
            futures.add(future);
        }
        // 모든 작업이 완료될 때까지 대기
        latch.await(10, TimeUnit.SECONDS);

        // Then
        List<OrderResponseDto> successOrders = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();
        for (Future<OrderResponseDto> future : futures) {
            try {
                OrderResponseDto result = future.get(1, TimeUnit.SECONDS);
                if (result != null) {
                    successOrders.add(result);
                }
            } catch (Exception e) {
                if (e instanceof ExecutionException) {
                    exceptions.add((Exception) e.getCause());
                } else {
                    exceptions.add(e);
                }
            }
        }

        // 최종 상품 상태 확인
        ProductEntity updatedProduct = productRepository.findById(product.getId()).orElseThrow();

        // 검증
        assertAll(
                // 남은 재고 수량 확인
                () -> assertEquals(0, updatedProduct.getAmount(),
                        "Final amount should be 0"),
                // 성공한 주문 수 확인
                () -> assertEquals(initialAmount, successOrders.size(),
                        "Should have exactly " + initialAmount + " successful orders"),
                // 실패한 주문 수 확인
                () -> assertEquals(threadCount - initialAmount, exceptions.size(),
                        "Should have exactly " + (threadCount - initialAmount) + " failed orders")
        );

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        assertTrue(terminated, "ExecutorService should terminate");
    }
}