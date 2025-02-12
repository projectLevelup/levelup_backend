package com.sparta.levelup_backend.domain.product.service;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {

	// 모든 상품 조회
	List<ProductEntity> getAllProducts();

	// 상품 ID로 조회
	Optional<ProductEntity> getProductById(Long id);

	// 새 상품 추가
	ProductEntity createProduct(ProductEntity product);

	// 상품 삭제
	void deleteProduct(Long id);

	// 특정 게임의 상품 조회
	List<ProductEntity> getProductsByGameId(Long gameId);
}

