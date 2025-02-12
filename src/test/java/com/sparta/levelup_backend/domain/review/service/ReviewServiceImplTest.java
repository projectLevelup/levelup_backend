package com.sparta.levelup_backend.domain.review.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.domain.review.repository.ReviewRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.UserRole;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void 리뷰_관리자_권한이_없을때_예외발생() {
        //given
        Long userId = 1L;
        Long productId = 1L;
        Long reviewId = 1L;

        UserEntity nomalUser = UserEntity.builder()
            .id(userId)
            .role(UserRole.USER)
            .build();

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(nomalUser));

        ReviewEntity review = ReviewEntity.builder()
            .id(reviewId)
            .build();

        //then
        assertThatThrownBy(() -> {
                reviewService.reviewDelete(userId, productId, reviewId);
            }).isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.FORBIDDEN_ACCESS.getMessage());

    }

    @Test
    void 리뷰와_상품의_매칭_확인() {
        //given
        Long userId = 1L;
        Long productId = 1L;
        Long otherProductId = 2L;
        Long reviewId = 1L;

        // 관리자 계정
        UserEntity user = UserEntity.builder()
            .id(userId)
            .role(UserRole.ADMIN)
            .build();

        ProductEntity product1 = ProductEntity.builder()
            .id(productId)
            .build();

        ProductEntity product2 = ProductEntity.builder()
            .id(otherProductId)
            .build();

        ReviewEntity review = ReviewEntity.builder()
            .id(reviewId)
            .product(product2)
            .build();

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        //then
        assertThatThrownBy(() -> {
            reviewService.reviewDelete(userId, productId, reviewId);
        }).isInstanceOf(BusinessException.class)
            .hasMessageContaining(ErrorCode.MISMATCH_REVIEW_PRODUCT.getMessage());
    }


}