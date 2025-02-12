package com.sparta.levelup_backend.domain.product.repository;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity; // ✅ ProductEntity import

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {  // ✅ ProductEntity 사용

    // 특정 게임에 속한 상품 조회
    List<ProductEntity> findByGameId(Long gameId);

    // 비관적 락 쿼리
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})  // 락 획특 시간 설정
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :productId")
    Optional<ProductEntity> findByIdWithLock(Long productId);
}
