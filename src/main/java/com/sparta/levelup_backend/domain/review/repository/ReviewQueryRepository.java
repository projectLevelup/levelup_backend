package com.sparta.levelup_backend.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.product.entity.QProductEntity;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewSliceResponseDto;
import com.sparta.levelup_backend.domain.review.entity.QReviewEntity;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.domain.user.entity.QUserEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public Slice<ReviewSliceResponseDto> findReviews(Long productId, Pageable pageable) {
        QReviewEntity review = new QReviewEntity("review");
        QProductEntity product = new QProductEntity("product");
        QUserEntity user = new QUserEntity("user");

        List<ReviewEntity> reviews = queryFactory
            .select(review)
            .from(review)
            .leftJoin(review.product, product).fetchJoin()
            .leftJoin(review.user, user).fetchJoin()
            .where(review.product.id.eq(productId))
            .orderBy(review.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        List<ReviewSliceResponseDto> reviewDto = reviews.stream()
            .map(reviewEntity -> new ReviewSliceResponseDto(
                reviewEntity.getId(),
                reviewEntity.getUser().getId(),
                reviewEntity.getContents(),
                reviewEntity.getStarScore()))
            .toList();

        boolean hasNext = reviews.size() > pageable.getPageSize();

        return new SliceImpl<>(reviewDto, pageable, hasNext);
    }

}
