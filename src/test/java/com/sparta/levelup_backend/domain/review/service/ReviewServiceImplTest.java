package com.sparta.levelup_backend.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.domain.review.repository.ReviewRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenAccessException;
import com.sparta.levelup_backend.utill.OrderStatus;
import com.sparta.levelup_backend.utill.UserRole;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    // 테스트별 공통으로 사용할 데이터
    private Long userId = 1L;
    private Long productId = 1L;
    private Long reviewId = 1L;
    private UserEntity normalUser;
    private UserEntity adminUser;
    private ProductEntity product;
    private ReviewEntity review;
    private OrderEntity completeOrder;

    @BeforeEach
    void setUp() {
        normalUser = UserEntity.builder()
            .id(userId)
            .role(UserRole.USER)
            .build();

        adminUser = UserEntity.builder()
            .id(userId)
            .role(UserRole.ADMIN)
            .build();

        product = ProductEntity.builder()
            .id(productId)
            .build();

        review = ReviewEntity.builder()
            .id(reviewId)
            .product(product)
            .build();

        completeOrder = OrderEntity.builder()
            .user(normalUser)
            .product(product)
            .status(OrderStatus.COMPLETED)
            .build();
    }

    @Test
    void 리뷰_관리자_권한이_없을때_예외발생() {
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(normalUser));

        //then
        assertThatThrownBy(() -> {
                reviewService.deleteReview(userId, productId, reviewId);
            }).isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.FORBIDDEN_ACCESS.getMessage());

    }

    @Test
    void 리뷰와_상품의_매칭_확인() {
        //given
        Long otherProductId = 2L;

        ProductEntity product2 = ProductEntity.builder()
            .id(otherProductId)
            .build();

        review = ReviewEntity.builder()
            .id(reviewId)
            .product(product2)
            .build();

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        //then
        assertThatThrownBy(() -> {
            reviewService.deleteReview(userId, productId, reviewId);
        }).isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.MISMATCH_REVIEW_PRODUCT.getMessage());
    }

    @Test
    @Transactional
    void 리뷰_삭제_테스트() {
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        reviewService.deleteReview(userId, productId, reviewId);

        //then
        assertThat(review.getIsDeleted()).isTrue();
    }

    @Test
    void 거래_완료된_주문이_없을때_예외발생() {
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(normalUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.existsByUserIdAndProductIdAndStatus(userId, productId, OrderStatus.COMPLETED)).thenReturn(false);

        //then
        assertThatThrownBy(() -> {
            reviewService.saveReview(new ReviewRequestDto("리뷰 테스트", 5), userId, productId);
        }).isInstanceOf(ForbiddenAccessException.class)
            .hasMessageContaining(ErrorCode.COMPLETED_ORDER_REQUIRED.getMessage());
    }

    @Test
    void 이미_작성된_리뷰가_있을때_예외발생() {
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(normalUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.existsByUserIdAndProductIdAndStatus(userId, productId, OrderStatus.COMPLETED)).thenReturn(true);
        when(reviewRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(true);

        //then
        assertThatThrownBy(() -> {
            reviewService.saveReview(new ReviewRequestDto("리뷰 테스트", 5), userId, productId);
        }).isInstanceOf(DuplicateException.class)
            .hasMessageContaining(ErrorCode.DUPLICATE_REVIEW.getMessage());

    }

}