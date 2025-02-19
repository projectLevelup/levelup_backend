package com.sparta.levelup_backend.domain.product.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductRequestAllDto;
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
		ProductCreateResponseDto productCreateResponseDto = productService.saveProduct(userId, dto);
		return success(OK, PRODUCT_CREATE, productCreateResponseDto);
	}

	@GetMapping
	public ApiResponse<List<ProductResponseDto>> getAllProducts() {
		List<ProductResponseDto> productList = productService.getAllProducts();
		return success(OK, PRODUCT_READ, productList);
	}

	@GetMapping("/{id}")
	public ApiResponse<ProductResponseDto> getProductById(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getId();
		ProductResponseDto responseDto = productService.getProductById(id, userId);
		return success(OK, PRODUCT_READ, responseDto);
	}

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

	@DeleteMapping("/{id}")
	public ApiResponse<ProductDeleteResponseDto> deleteProduct(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		ProductDeleteResponseDto responseDto = productService.deleteProduct(id, userId);
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

	@GetMapping("/search")
	public ApiResponse<List<ProductDocument>> getAllProductsES() {
		List<ProductDocument> productList = productService.getAllProductsES();
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, productList);
	}

	@GetMapping("/search/{id}")
	public ApiResponse<ProductDocument> getProductByIdES(@PathVariable Long id) {
		ProductDocument responseDto = productService.getProductByIdES(id);
		return success(OK, PRODUCT_READ, responseDto);
	}

	/**
	 * 상품명 검색 (부분 검색)
	 * GET /search/productName
	 */
	@GetMapping("/search/productName")
	public ResponseEntity<List<ProductDocument>> searchByProductName(@RequestParam String productName) {
		List<ProductDocument> products = productService.searchByProductNameES(productName);
		return ResponseEntity.ok(products);
	}

	/**
	 * 특정 게임에 속한 상품 검색
	 * GET /search/game/{gameId}
	 */
	@GetMapping("/game/{gameId}")
	public ApiResponse<List<ProductDocument>> searchByGameId(@PathVariable Long gameId) {
		List<ProductDocument> products = productService.searchByGameIdES(gameId);
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, products);
	}

	/**
	 * 특정 상태의 상품 검색
	 * GET /search/status/{productStatus}
	 */
	@GetMapping("/status/{productStatus}")
	public ApiResponse<List<ProductDocument>> searchByStatus(@PathVariable String productStatus) {
		List<ProductDocument> products = productService.searchByStatusES(productStatus);
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, products);
	}

	/**
	 * 특정 사용자가 등록한 상품 조회
	 * GET /search/user/{userId}
	 */
	@GetMapping("/user/{userId}")
	public ApiResponse<List<ProductDocument>> searchByUserId(@PathVariable Long userId) {
		List<ProductDocument> products = productService.searchByUserIdES(userId);
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, products);
	}

	/**
	 * 카테고리별 상품 개수 집계
	 * GET /search/aggregations/category
	 */
	@GetMapping("/aggregations/category")
	public ApiResponse<Map<String, Long>> getCategoryAggregations() {
		Map<String, Long> categoryCounts = productService.getGenreAggregationsES();
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, categoryCounts);
	}

	/**
	 * 감성 분석 결과 상위 3개 상품 조회 API
	 * GET /products/top3
	 */
	@GetMapping("/top3")
	public ApiResponse<List<ProductRequestAllDto>> getTop3Products() {
		List<ProductRequestAllDto> top3Products = productService.getTop3Products();
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, top3Products);
	}

	/**
	 * 인기 상품 Top 10 반환 API
	 * GET /search/popular
	 */
	@GetMapping("/popular")
	public ApiResponse<List<ProductDocument>> getTop10PopularProducts() {
		List<ProductDocument> top10Products = productService.getTop10PopularProductsES();
		return ApiResponse.success(HttpStatus.OK, PRODUCT_READ, top10Products);
	}
}

