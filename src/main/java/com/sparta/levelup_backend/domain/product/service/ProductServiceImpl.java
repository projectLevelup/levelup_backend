package com.sparta.levelup_backend.domain.product.service;

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
