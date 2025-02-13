package com.sparta.levelup_backend.domain.product.service;

import java.util.List;

import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;

public interface ProductService {

	List<ProductResponseDto> getAllProducts(Long userId);

	ProductResponseDto getProductById(Long id, Long userId);

	ProductCreateResponseDto saveProduct(Long id, ProductCreateRequestDto dto);

	ProductUpdateResponseDto updateProduct(Long id, Long userId, ProductUpdateRequestDto requestDto);

	ProductDeleteResponseDto deleteProduct(Long id, Long userId);

}

