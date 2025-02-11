package com.sparta.levelup_backend.domain.product.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    // 모든 상품 조회
    @GetMapping
    public List<ProductEntity> getAllProducts() {
        return productServiceImpl.getAllProducts();
    }

    // 상품 ID로 조회
    @GetMapping("/{id}")
    public Optional<ProductEntity> getProductById(@PathVariable Long id) {
        return productServiceImpl.getProductById(id);
    }

    // 새 상품 추가
    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productServiceImpl.createProduct(product);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productServiceImpl.deleteProduct(id);
    }
}

