package com.sparta.levelup_backend.domain.product.service;

import java.util.List;
import java.util.Map;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.dto.request.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.request.ProductRequestAllDto;
import com.sparta.levelup_backend.domain.product.dto.request.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductUpdateResponseDto;

public interface ProductService {

	List<ProductResponseDto> getAllProducts();

	ProductResponseDto getProductById(Long id, Long userId);

	ProductCreateResponseDto saveProduct(Long id, ProductCreateRequestDto dto);

	ProductUpdateResponseDto updateProduct(Long id, Long userId, ProductUpdateRequestDto requestDto);

	ProductDeleteResponseDto deleteProduct(Long id, Long userId);

	List<ProductDocument> getAllProductsES();

	ProductDocument getProductByIdES(Long id);

	List<ProductDocument> searchByProductNameES(String productName);

	List<ProductDocument> searchByGameIdES(Long gameId);

	List<ProductDocument> searchByStatusES(String status);

	List<ProductDocument> searchByUserIdES(Long userId);

	Map<String, Long> getGenreAggregationsES();

	List<ProductDocument> getTop10PopularProductsES();

	List<ProductRequestAllDto> getTop3Products();
}

