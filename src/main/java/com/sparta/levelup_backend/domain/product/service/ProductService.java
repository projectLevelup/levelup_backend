package com.sparta.levelup_backend.domain.product.service;

import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import java.util.List;

public interface ProductService {

	List<ProductResponseDto> getAllProducts();
	ProductResponseDto getProductById(Long id);
	ProductCreateResponseDto productCreate(ProductCreateRequestDto dto);
	ProductUpdateResponseDto updateProduct(Long id, ProductUpdateRequestDto requestDto);
	ProductDeleteResponseDto deleteProduct(Long id);

	List<ProductEntity> getProductsByGameId(Long gameId);


}

