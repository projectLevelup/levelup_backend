package com.sparta.levelup_backend.domain.product.repository;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity; // ✅ ProductEntity import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, BigInteger> {  // ✅ ProductEntity 사용

    // 특정 게임에 속한 상품 조회
    List<ProductEntity> findByGameId(BigInteger gameId);

}
