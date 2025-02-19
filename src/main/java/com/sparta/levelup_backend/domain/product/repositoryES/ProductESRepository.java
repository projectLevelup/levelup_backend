package com.sparta.levelup_backend.domain.product.repositoryES;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.utill.ProductStatus;

@Repository
public interface ProductESRepository
	extends ElasticsearchRepository<ProductDocument, String> {

	List<ProductDocument> findByGameIdAndIsDeletedFalseAndStatus(Long gameId, ProductStatus status);

	List<ProductDocument> findByStatusAndIsDeletedFalse(String statusText, ProductStatus statusEnum);

	List<ProductDocument> findByUserIdAndIsDeletedFalseAndStatus(Long userId, ProductStatus status);

	ProductDocument findByProductIdAndIsDeletedFalseAndStatus(Long productId, ProductStatus status);

	List<ProductDocument> findByIsDeletedFalseAndStatus(ProductStatus status);

	List<ProductDocument> findAllByIsDeletedFalseAndStatus(ProductStatus productStatus);
}
