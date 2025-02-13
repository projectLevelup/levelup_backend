package com.sparta.levelup_backend.domain.product.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
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
		ProductCreateResponseDto productCreateResponseDto = productService.saveProduct(userId, dto);
		return success(OK, PRODUCT_CREATE, productCreateResponseDto);
	}

	@GetMapping
	public ApiResponse<List<ProductResponseDto>> getAllProducts(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		List<ProductResponseDto> productList = productService.getAllProducts(userId);
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

}

