package com.sparta.levelup_backend.domain.product.service;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 모든 상품 조회
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 ID로 조회
    public Optional<ProductEntity> getProductById(BigInteger id) {
        return productRepository.findById(id);
    }

    // 새 상품 추가
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    // 상품 삭제
    public void deleteProduct(BigInteger id) {
        productRepository.deleteById(id);
    }

    // 특정 게임의 상품 조회
    public List<ProductEntity> getProductsByGameId(BigInteger gameId) {
        return productRepository.findByGameId(gameId);
    }
}
