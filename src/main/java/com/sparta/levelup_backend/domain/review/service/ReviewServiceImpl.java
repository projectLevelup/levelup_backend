package com.sparta.levelup_backend.domain.review.service;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.domain.review.repository.ReviewRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ReviewResponseDto reviewSave(ReviewRequestDto dto, Long userId, Long productId) {

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
}
