package com.sparta.levelup_backend.domain.review.service;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.domain.review.repository.ReviewQueryRepository;
import com.sparta.levelup_backend.domain.review.repository.ReviewRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenAccessException;
import com.sparta.levelup_backend.exception.common.MismatchException;
import com.sparta.levelup_backend.utill.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ReviewResponseDto saveReview(ReviewRequestDto dto, Long userId, Long productId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);

        ReviewEntity review = ReviewEntity.builder()
            .contents(dto.getContents())
            .starScore(dto.getStarScore())
            .user(user)
            .product(product)
            .build();

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long userId, Long productId, Long reviewId) {

        UserEntity user = userRepository.findById(userId).orElseThrow(RuntimeException::new);

        // 리뷰 삭제는 관리자 권한만 실행 가능
        if(!user.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenAccessException(ErrorCode.FORBIDDEN_ACCESS);
        }

        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(RuntimeException::new);

        //리뷰가 해당 상품의 리뷰인 지 확인
        if(!review.getProduct().getId().equals(productId)) {
            throw new MismatchException(ErrorCode.MISMATCH_REVIEW_PRODUCT);
        }

        review.DeleteReview();

    }

    @Override
    public Slice<ReviewResponseDto> findReviews(Long productId, Pageable pageable) {
        return reviewQueryRepository.findReviews(productId, pageable);
    }
}
