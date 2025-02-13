package com.sparta.levelup_backend.domain.product.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.service.ProductService;
import com.sparta.levelup_backend.domain.product.service.ProductmakedataService;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductmakedataService productmakedataService;

	public ProductController(ProductService productService, ProductmakedataService productmakedataService) {
		this.productService = productService;
		this.productmakedataService = productmakedataService;
	}

    @PostMapping
    public ApiResponse<ProductCreateResponseDto> saveProduct(
        @Valid @RequestBody ProductCreateRequestDto dto
    ) {
        ProductCreateResponseDto productCreateResponseDto = productService.saveProduct(dto);
        return success(OK, PRODUCT_CREATE, productCreateResponseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> productList = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK)
            .body(success(OK, PRODUCT_READ, productList));
    }


    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto responseDto = productService.getProductById(id);
        return success(OK, PRODUCT_READ, responseDto);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ProductUpdateResponseDto> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductUpdateRequestDto requestDto
    ) {
        ProductUpdateResponseDto responseDto = productService.updateProduct(id, requestDto);
        return success(OK, PRODUCT_UPDATE, responseDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ProductDeleteResponseDto> deleteProduct(@PathVariable Long id) {
        ProductDeleteResponseDto responseDto = productService.deleteProduct(id);
        return success(OK, PRODUCT_DELETE, responseDto);
    }




    @PostMapping("/users/{count}")
    public ResponseEntity<String> createUsers(@PathVariable int count) {
        productmakedataService.generateUsers(count);
        return ResponseEntity.ok(count + "명의 유저 데이터가 생성되었습니다.");
    }

    @PostMapping("/games/{count}")
    public ResponseEntity<String> createGames(@PathVariable int count) {
        productmakedataService.generateGames(count);
        return ResponseEntity.ok(count + "개의 게임 데이터가 생성되었습니다.");
    }

    @PostMapping("/products/{count}")
    public ResponseEntity<String> createProducts(@PathVariable int count) {
        productmakedataService.generateProducts(count);
        return ResponseEntity.ok(count + "개의 상품 데이터가 생성되었습니다.");
    }

}

