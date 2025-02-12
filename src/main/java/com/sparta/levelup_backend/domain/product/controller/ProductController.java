package com.sparta.levelup_backend.domain.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.service.ProductService;
import com.sparta.levelup_backend.domain.product.service.ProductmakedataService;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;
    private ProductmakedataService productmakedataService;
    private ProductServiceImpl productServiceImpl;

    // 모든 상품 조회
    @GetMapping
    public List<ProductEntity> getAllProducts() {
        return productService.getAllProducts();
    }

    // 상품 ID로 조회
    @GetMapping("/{id}")
    public Optional<ProductEntity> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productService.createProduct(product);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // 상품 100만개 등록
    @GetMapping("/generate")
    public ResponseEntity<String> generateProducts(@RequestParam(defaultValue = "100000") int count) {
        productmakedataService.generateProducts(count); // ✅ static 호출이 아니라 인스턴스 호출로 변경
        return ResponseEntity.ok(count + "개의 제품 데이터가 생성되었습니다.");
    }
}

