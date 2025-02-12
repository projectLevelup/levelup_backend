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
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductmakedataService productmakedataService;
    private final ProductServiceImpl productServiceImpl;

	public ProductController(ProductService productService, ProductmakedataService productmakedataService,
		ProductServiceImpl productServiceImpl) {
		this.productService = productService;
		this.productmakedataService = productmakedataService;
		this.productServiceImpl = productServiceImpl;
	}

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponseDto>> productCreate(
        @Valid @RequestBody ProductCreateRequestDto dto
    ) {
        ProductCreateResponseDto productCreateResponseDto = productService.productCreate(dto);
        return ResponseEntity.status(HttpStatus.OK)
            .body(success(OK, PRODUCT_CREATE, productCreateResponseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> productList = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK)
            .body(success(OK, PRODUCT_READ, productList));
    }

    // ✅ 상품 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        ProductResponseDto responseDto = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(success(OK, PRODUCT_READ, responseDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductUpdateResponseDto>> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductUpdateRequestDto requestDto
    ) {
        ProductUpdateResponseDto responseDto = productService.updateProduct(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
            .body(success(OK, PRODUCT_UPDATE, responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDeleteResponseDto>> deleteProduct(@PathVariable Long id) {
        // delete 후 삭제된 상품 id나 메시지를 담을 수 있는 DTO 반환
        ProductDeleteResponseDto responseDto = productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(success(OK, PRODUCT_DELETE, responseDto));
    }


    // 상품 100만개 등록
    @GetMapping("/generate")
    public ResponseEntity<String> generateProducts(@RequestParam(defaultValue = "100000") int count) {
        productmakedataService.generateProducts(count); // ✅ static 호출이 아니라 인스턴스 호출로 변경
        return ResponseEntity.ok(count + "개의 제품 데이터가 생성되었습니다.");
    }
}

