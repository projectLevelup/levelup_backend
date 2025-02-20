package com.sparta.levelup_backend.domain.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;

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

	List<ProductDocument> getTop3Products() throws IOException;
}

