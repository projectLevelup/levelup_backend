package com.sparta.levelup_backend.domain.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.dto.request.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.request.ProductRequestAllDto;
import com.sparta.levelup_backend.domain.product.dto.request.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.response.ProductUpdateResponseDto;


public interface ProductService {

	Page<ProductResponseDto> getAllProducts(Pageable pageable);

	Page<ProductResponseDto> getAllProductsByUser(Long userId, Pageable pageable);

	ProductResponseDto getProductById(Long id, Long userId);

	ProductCreateResponseDto saveProduct(Long id, ProductCreateRequestDto dto, MultipartFile image);

	ProductUpdateResponseDto updateProduct(Long id, Long userId, ProductUpdateRequestDto requestDto, MultipartFile image);

	ProductDeleteResponseDto deleteProduct(Long id, Long userId);

	Page<ProductDocument> getAllProductsES(Pageable pageable);

	ProductDocument getProductByIdES(Long id);

	Page<ProductDocument> searchByProductNameES(String productName, Pageable pageable);

	Page<ProductDocument> searchByGameIdES(Long gameId, Pageable pageable);

	Page<ProductDocument> searchByStatusES(String status, Pageable pageable);

	Page<ProductDocument> searchByUserIdES(Long userId, Pageable pageable);

	Map<String, Long> getGenreAggregationsES();

	Page<ProductDocument> getTop10PopularProductsES(Pageable pageable);

	Page<ProductRequestAllDto> getTop3Products(Pageable pageable);
}

