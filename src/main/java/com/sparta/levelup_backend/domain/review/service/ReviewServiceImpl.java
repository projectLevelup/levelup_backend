package com.sparta.levelup_backend.domain.review.service;

import static com.sparta.levelup_backend.enums.ErrorCode.*;
import static com.sparta.levelup_backend.enums.OrderStatus.*;
import static com.sparta.levelup_backend.enums.UserRole.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.domain.review.repository.ReviewQueryRepository;
import com.sparta.levelup_backend.domain.review.repository.ReviewRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.review.ReviewException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewQueryRepository reviewQueryRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Override
	@Transactional
	public ReviewResponseDto saveReview(ReviewRequestDto dto, Long userId, Long productId) {

		if (!orderRepository.existsByUserIdAndProductIdAndStatus(userId, productId, COMPLETED)) {
			throw new ReviewException(COMPLETED_ORDER_REQUIRED);
		}

		if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
			throw new ReviewException(DUPLICATE_REVIEW);
		}

		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		ProductEntity product = productRepository.findByIdOrElseThrow(productId);

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

		UserEntity user = userRepository.findByIdOrElseThrow(userId);

		if (!user.getRole().equals(ADMIN)) {
			throw new ReviewException(FORBIDDEN_ACCESS);
		}

		ReviewEntity review = reviewRepository.findByIdOrElseThrow(reviewId);

		if (review.getIsDeleted()) {
			throw new ReviewException(REVIEW_ISDELETED);
		}

		if (!review.getProduct().getId().equals(productId)) {
			throw new ReviewException(MISMATCH_REVIEW_PRODUCT);
		}

		review.deleteReview();
	}

	@Override
	public Slice<ReviewResponseDto> findReviews(Long productId, Pageable pageable) {
		return reviewQueryRepository.findReviews(productId, pageable);
	}
}
