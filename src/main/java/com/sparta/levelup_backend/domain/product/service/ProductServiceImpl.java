package com.sparta.levelup_backend.domain.product.service;

import java.util.List;
import java.util.Optional;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    // 모든 상품 조회
    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 ID로 조회
    @Override
    public Optional<ProductEntity> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // 새 상품 추가
    @Override
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    // 상품 삭제
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // 특정 게임의 상품 조회
    @Override
    public List<ProductEntity> getProductsByGameId(Long gameId) {
        return productRepository.findByGameId(gameId);
    }

    @Transactional(timeout = 5, rollbackFor = Exception.class)
    public void decreaseAmount(Long productId) {
        ProductEntity product = getFindByIdWithLock(productId);
        product.decreaseAmount();
        productRepository.save(product);
    }

    @Transactional(timeout = 5, rollbackFor = Exception.class)
    public void increaseAmount(Long productId) {
        ProductEntity product = getFindByIdWithLock(productId);
        product.increaseAmount();
        productRepository.save(product);
    }

    public ProductEntity getFindByIdWithLock(Long productId) {
        return productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public ProductEntity findById(Long productId) {
      return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
