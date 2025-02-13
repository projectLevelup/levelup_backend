package com.sparta.levelup_backend.domain.product.service;

import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;

import java.util.List;

public interface ProductService {

	List<ProductResponseDto> getAllProducts();
	ProductResponseDto getProductById(Long id);
	ProductCreateResponseDto saveProduct(ProductCreateRequestDto dto);
	ProductUpdateResponseDto updateProduct(Long id, ProductUpdateRequestDto requestDto);
	ProductDeleteResponseDto deleteProduct(Long id);

}

