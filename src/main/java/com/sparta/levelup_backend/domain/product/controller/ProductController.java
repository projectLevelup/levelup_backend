package com.sparta.levelup_backend.domain.product.controller;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.dto.request.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.request.ProductRequestAllDto;
import com.sparta.levelup_backend.domain.product.dto.request.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ApiResponse<ProductCreateResponseDto> saveProduct(
		@Valid @RequestBody ProductCreateRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		return success(OK, PRODUCT_CREATE, productService.saveProduct(userId, dto));
	}

	// 전체 상품 조회 (페이징)
	@GetMapping
	public ApiResponse<Page<ProductResponseDto>> findAllProducts(Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.getAllProducts(pageable));
	}

	// 특정 사용자가 등록한 상품 조회 (페이징)
	@GetMapping("/mine/{userId}")
	public ApiResponse<Page<ProductResponseDto>> findAllProductsByUser(@PathVariable Long userId, Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.getAllProductsByUser(userId, pageable));
	}

	// 상품 ID로 조회
	@GetMapping("/{id}")
	public ApiResponse<ProductResponseDto> findProductById(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		return success(OK, PRODUCT_READ, productService.getProductById(id, userId));
	}

	// 상품 수정
	@PatchMapping("/{id}")
	public ApiResponse<ProductUpdateResponseDto> updateProduct(
		@PathVariable Long id,
		@Valid @RequestBody ProductUpdateRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		return success(OK, PRODUCT_UPDATE, productService.updateProduct(id, userId, requestDto));
	}

	// 상품 삭제
	@DeleteMapping("/{id}")
	public ApiResponse<ProductDeleteResponseDto> deleteProduct(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		return success(OK, PRODUCT_DELETE, productService.deleteProduct(id, userId));
	}

	// Elasticsearch - 전체 상품 검색 (페이징)
	@GetMapping("/all")
	public ApiResponse<Page<ProductDocument>> findAllProductsES(Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.getAllProductsES(pageable));
	}

	// Elasticsearch - 상품 ID 조회
	@GetMapping("/es/{id}")
	public ApiResponse<ProductDocument> findProductByIdES(@PathVariable Long id) {
		return success(OK, PRODUCT_READ, productService.getProductByIdES(id));
	}

	// Elasticsearch - 상품명 검색 (페이징)
	@GetMapping("/productName")
	public ApiResponse<Page<ProductDocument>> findProductsByName(
		@RequestParam String productName,
		Pageable pageable
	) {
		return success(OK, PRODUCT_READ, productService.searchByProductNameES(productName, pageable));
	}

	// Elasticsearch - 특정 게임에 속한 상품 조회 (페이징)
	@GetMapping("/game/{gameId}")
	public ApiResponse<Page<ProductDocument>> findProductsByGameId(@PathVariable Long gameId, Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.searchByGameIdES(gameId, pageable));
	}

	// Elasticsearch - 특정 상태의 상품 조회 (페이징)
	@GetMapping("/status/{productStatus}")
	public ApiResponse<Page<ProductDocument>> findProductsByStatus(@PathVariable String productStatus, Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.searchByStatusES(productStatus, pageable));
	}

	// Elasticsearch - 특정 사용자가 등록한 상품 조회 (페이징)
	@GetMapping("/user/{userId}")
	public ApiResponse<Page<ProductDocument>> findProductsByUserId(@PathVariable Long userId, Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.searchByUserIdES(userId, pageable));
	}

	// Elasticsearch - 카테고리별 상품 개수 집계
	@GetMapping("/aggregations/category")
	public ApiResponse<Map<String, Long>> findCategoryAggregations() {
		return success(OK, PRODUCT_READ, productService.getGenreAggregationsES());
	}

	// Elasticsearch - 감성 분석 결과 상위 3개 상품 조회 (페이징)
	@GetMapping("/sentimentanalysis/top3")
	public ApiResponse<Page<ProductRequestAllDto>> findTop3Products(Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.getTop3Products(pageable));
	}

	// Elasticsearch - 인기 상품 Top 10 조회 (페이징)
	@GetMapping("/aggregations/popular")
	public ApiResponse<Page<ProductDocument>> findTop10PopularProducts(Pageable pageable) {
		return success(OK, PRODUCT_READ, productService.getTop10PopularProductsES(pageable));
	}
}
