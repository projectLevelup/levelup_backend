package com.sparta.levelup_backend.domain.product.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
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

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.service.ProductService;
import com.sparta.levelup_backend.domain.product.service.ProductmakedataService;

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
		@Valid @RequestBody ProductCreateRequestDto dto,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		ProductCreateResponseDto responseDto = productService.saveProduct(userId, dto);
		return success(OK, PRODUCT_CREATE, responseDto);
	}

	// 전체 상품 조회 → findAllProducts
	@GetMapping
	public ApiResponse<List<ProductResponseDto>> findAllProducts() {
		List<ProductResponseDto> productList = productService.getAllProducts();
		return success(OK, PRODUCT_READ, productList);
	}

	// 상품 ID로 상품 조회 → findProductById
	@GetMapping("/{id}")
	public ApiResponse<ProductResponseDto> findProductById(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		ProductResponseDto responseDto = productService.getProductById(id, userId);
		return success(OK, PRODUCT_READ, responseDto);
	}

	// 상품 수정
	@PatchMapping("/{id}")
	public ApiResponse<ProductUpdateResponseDto> updateProduct(
		@PathVariable Long id,
		@Valid @RequestBody ProductUpdateRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		ProductUpdateResponseDto responseDto = productService.updateProduct(id, userId, requestDto);
		return success(OK, PRODUCT_UPDATE, responseDto);
	}

	// 상품 삭제
	@DeleteMapping("/{id}")
	public ApiResponse<ProductDeleteResponseDto> deleteProduct(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		ProductDeleteResponseDto responseDto = productService.deleteProduct(id, userId);
		return success(OK, PRODUCT_DELETE, responseDto);
	}

	// 유저 데이터 생성 (테스트용)
	@PostMapping("/users/{count}")
	public ResponseEntity<String> generateUsers(@PathVariable int count) {
		productmakedataService.generateUsers(count);
		return ResponseEntity.ok(count + "명의 유저 데이터가 생성되었습니다.");
	}

	// 게임 데이터 생성 (테스트용)
	@PostMapping("/games/{count}")
	public ResponseEntity<String> generateGames(@PathVariable int count) {
		productmakedataService.generateGames(count);
		return ResponseEntity.ok(count + "개의 게임 데이터가 생성되었습니다.");
	}

	// 상품 데이터 생성 (테스트용)
	@PostMapping("/products/{count}")
	public ResponseEntity<String> generateProducts(@PathVariable int count) {
		productmakedataService.generateProducts(count);
		return ResponseEntity.ok(count + "개의 상품 데이터가 생성되었습니다.");
	}

	// Elasticsearch를 활용한 전체 상품 검색 (ES)
	@GetMapping("/es")
	public ApiResponse<List<ProductDocument>> findAllProductsES() {
		List<ProductDocument> productList = productService.getAllProductsES();
		return ApiResponse.success(OK, PRODUCT_READ, productList);
	}

	// Elasticsearch를 활용한 상품 ID로 상품 조회 (ES)
	@GetMapping("/es/{id}")
	public ApiResponse<ProductDocument> findProductByIdES(@PathVariable Long id) {
		ProductDocument product = productService.getProductByIdES(id);
		return success(OK, PRODUCT_READ, product);
	}

	/**
	 * 상품명으로 상품 부분 검색 (ES)
	 * GET /v1/products/es/productName?productName=...
	 */
	@GetMapping("/es/productName")
	public ResponseEntity<List<ProductDocument>> findProductsByName(@RequestParam String productName) {
		List<ProductDocument> products = productService.searchByProductNameES(productName);
		return ResponseEntity.ok(products);
	}

	/**
	 * 특정 게임에 속한 상품 조회 (ES)
	 * GET /v1/products/es/game/{gameId}
	 */
	@GetMapping("/es/game/{gameId}")
	public ApiResponse<List<ProductDocument>> findProductsByGameId(@PathVariable Long gameId) {
		List<ProductDocument> products = productService.searchByGameIdES(gameId);
		return ApiResponse.success(OK, PRODUCT_READ, products);
	}

	/**
	 * 특정 상태의 상품 조회 (ES)
	 * GET /v1/products/es/status/{productStatus}
	 */
	@GetMapping("/es/status/{productStatus}")
	public ApiResponse<List<ProductDocument>> findProductsByStatus(@PathVariable String productStatus) {
		List<ProductDocument> products = productService.searchByStatusES(productStatus);
		return ApiResponse.success(OK, PRODUCT_READ, products);
	}

	/**
	 * 특정 사용자가 등록한 상품 조회 (ES)
	 * GET /v1/products/es/user/{userId}
	 */
	@GetMapping("/es/user/{userId}")
	public ApiResponse<List<ProductDocument>> findProductsByUserId(@PathVariable Long userId) {
		List<ProductDocument> products = productService.searchByUserIdES(userId);
		return ApiResponse.success(OK, PRODUCT_READ, products);
	}

	/**
	 * 카테고리별 상품 개수 집계 (ES)
	 * GET /v1/products/es/aggregations/category
	 */
	@GetMapping("/es/aggregations/category")
	public ApiResponse<Map<String, Long>> findCategoryAggregations() {
		Map<String, Long> categoryCounts = productService.getGenreAggregationsES();
		return ApiResponse.success(OK, PRODUCT_READ, categoryCounts);
	}

	/**
	 * 감성 분석 결과 상위 3개 상품 조회 (ES)
	 * GET /v1/products/es/sentimentanalysis/top3
	 */
	@GetMapping("/es/sentimentanalysis/top3")
	public ApiResponse<List<ProductDocument>> findTop3Products() throws IOException {
		List<ProductDocument> top3Products = productService.getTop3Products();
		return ApiResponse.success(OK, PRODUCT_READ, top3Products);
	}

	/**
	 * 인기 상품 Top 10 조회 (ES)
	 * GET /v1/products/es/aggregations/popular
	 */
	@GetMapping("/es/aggregations/popular")
	public ApiResponse<List<ProductDocument>> findTop10PopularProducts() {
		List<ProductDocument> top10Products = productService.getTop10PopularProductsES();
		return ApiResponse.success(OK, PRODUCT_READ, top10Products);
	}
}
