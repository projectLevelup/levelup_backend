package com.sparta.levelup_backend.domain.product.controller;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	// 전체 상품 조회 → findAllProducts
	@GetMapping
	public ApiResponse<List<ProductResponseDto>> findAllProducts() {
		return success(OK, PRODUCT_READ, productService.getAllProducts());
	}

	// 상품 ID로 상품 조회 → findProductById
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

	// Elasticsearch를 활용한 전체 상품 검색 (ES)
	@GetMapping("/all")
	public ApiResponse<List<ProductDocument>> findAllProductsES() {
		return success(OK, PRODUCT_READ, productService.getAllProductsES());
	}

	// Elasticsearch를 활용한 상품 ID로 상품 조회 (ES)
	@GetMapping("/{id}")
	public ApiResponse<ProductDocument> findProductByIdES(@PathVariable Long id) {
		return success(OK, PRODUCT_READ, productService.getProductByIdES(id));
	}

	/**
	 * 상품명으로 상품 부분 검색 (ES)
	 * GET /v1/products/productName?productName=...
	 */
	@GetMapping("/productName")
	public ApiResponse<List<ProductDocument>> findProductsByName(@RequestParam String productName) {
		return success(OK, PRODUCT_READ,productService.searchByProductNameES(productName));
	}

	/**
	 * 특정 게임에 속한 상품 조회 (ES)
	 * GET /v1/products/game/{gameId}
	 */
	@GetMapping("/game/{gameId}")
	public ApiResponse<List<ProductDocument>> findProductsByGameId(@PathVariable Long gameId) {
		return success(OK, PRODUCT_READ, productService.searchByGameIdES(gameId));
	}

	/**
	 * 특정 상태의 상품 조회 (ES)
	 * GET /v1/products/status/{productStatus}
	 */
	@GetMapping("/status/{productStatus}")
	public ApiResponse<List<ProductDocument>> findProductsByStatus(@PathVariable String productStatus) {
		return success(OK, PRODUCT_READ, productService.searchByStatusES(productStatus));
	}

	/**
	 * 특정 사용자가 등록한 상품 조회 (ES)
	 * GET /v1/products/user/{userId}
	 */
	@GetMapping("/user/{userId}")
	public ApiResponse<List<ProductDocument>> findProductsByUserId(@PathVariable Long userId) {
		return success(OK, PRODUCT_READ, productService.searchByUserIdES(userId));
	}

	/**
	 * 카테고리별 상품 개수 집계 (ES)
	 * GET /v1/products/aggregations/category
	 */
	@GetMapping("/aggregations/category")
	public ApiResponse<Map<String, Long>> findCategoryAggregations() {
		return success(OK, PRODUCT_READ, productService.getGenreAggregationsES());
	}

	/**
	 * 감성 분석 결과 상위 3개 상품 조회 (ES)
	 * GET /v1/products/sentimentanalysis/top3
	 */
	@GetMapping("/sentimentanalysis/top3")
	public ApiResponse<List<ProductRequestAllDto>> findTop3Products() {
		return success(OK, PRODUCT_READ, productService.getTop3Products());
	}

	/**
	 * 인기 상품 Top 10 조회 (ES)
	 * GET /v1/products/aggregations/popular
	 */
	@GetMapping("/aggregations/popular")
	public ApiResponse<List<ProductDocument>> findTop10PopularProducts() {
		return success(OK, PRODUCT_READ, productService.getTop10PopularProductsES());
	}
}
