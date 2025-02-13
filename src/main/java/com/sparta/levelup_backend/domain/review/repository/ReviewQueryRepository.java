package com.sparta.levelup_backend.domain.review.repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.product.entity.QProductEntity;
import com.sparta.levelup_backend.domain.review.dto.response.QReviewResponseDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;
import com.sparta.levelup_backend.domain.review.entity.QReviewEntity;
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

    /**
     * 특정 상품의 리뷰 목록을 페이징하여 조회
     *
     * @return 다음 페이지 여부를 포함하는 Slice 형태의 리뷰 목록
     */
    public Slice<ReviewResponseDto> findReviews(Long productId, Pageable pageable) {
        QReviewEntity review = new QReviewEntity("review");
        QProductEntity product = new QProductEntity("product");
        QUserEntity user = new QUserEntity("user");

        List<ReviewResponseDto> reviews = queryFactory
            .select(new QReviewResponseDto(
                review.id,
                user.id,
                user.nickName,
                review.contents,
                review.starScore))
            .from(review)
            .leftJoin(review.product, product)
            .leftJoin(review.user, user)
            .where(review.product.id.eq(productId))
            .where(review.isDeleted.eq(false))
            .orderBy(review.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // 다음 페이지 여부 확인
        boolean hasNext = reviews.size() > pageable.getPageSize();

        return new SliceImpl<>(reviews, pageable, hasNext);
    }

}
