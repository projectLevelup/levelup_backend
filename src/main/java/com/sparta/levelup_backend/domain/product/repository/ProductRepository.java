package com.sparta.levelup_backend.domain.product.repository;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.enums.ProductStatus;
import com.sparta.levelup_backend.exception.product.ProductException;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	// 페이징 적용: 활성화된 삭제되지 않은 모든 제품 조회
	Page<ProductEntity> findAllByIsDeletedFalseAndStatus(ProductStatus status, Pageable pageable);

	// ID로 조회, 없으면 예외 발생
	default ProductEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
	}

	// 비관적 락을 적용한 조회 (트랜잭션 환경에서 사용)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})  // 락 획득 시간 설정
	@Query("SELECT p FROM ProductEntity p WHERE p.id = :productId")
	Optional<ProductEntity> findByIdWithLock(Long productId);

	// 비관적 락 적용 후 ID 조회, 없으면 예외 발생
	default ProductEntity findByIdWithLockOrElseThrow(Long productId) {
		return findByIdWithLock(productId)
			.orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
	}

	// 특정 사용자가 생성한 삭제되지 않은 제품 조회 (페이징 적용)
	Page<ProductEntity> findAllByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
}
