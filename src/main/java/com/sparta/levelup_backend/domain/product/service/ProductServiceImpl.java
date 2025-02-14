package com.sparta.levelup_backend.domain.product.service;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.utill.ProductStatus;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final GameRepository gameRepository;

	@Override
	public List<ProductResponseDto> getAllProducts() {
		return productRepository.findAllByIsDeletedFalseAndStatus(ProductStatus.ACTIVE)
			.stream()
			.map(ProductResponseDto::new)
			.collect(Collectors.toList());
	}

	@Override
	public ProductResponseDto getProductById(Long id) {
		return productRepository.findByIdAndIsDeletedFalseAndStatus(id, ProductStatus.ACTIVE)
			.map(ProductResponseDto::new)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "상품 ID: " + id));
	}

	@Transactional
	@Override
	public ProductCreateResponseDto saveProduct(Long userId, ProductCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		GameEntity game = gameRepository.findByIdOrElseThrow(dto.getGameId());

		ProductEntity product = new ProductEntity(dto, user, game);
		ProductEntity savedProduct = productRepository.save(product);

		return new ProductCreateResponseDto(savedProduct);
	}

	@Transactional
	@Override
	public ProductUpdateResponseDto updateProduct(Long id, Long userId, ProductUpdateRequestDto requestDto) {
		ProductEntity product = productRepository.findByIdOrElseThrow(id);
		UserEntity user = userRepository.findByIdOrElseThrow(userId);

		// 수정 권한 체크 (본인이 등록한 상품만 수정 가능)
		if (!product.getUser().getId().equals(user.getId()) && !user.getRole().equals(UserRole.ADMIN)) {
			throw new DuplicateException(FORBIDDEN_ACCESS);
		}

		// STATUS가 ACTIVE인 상품만 수정 가능
		if (product.getStatus() != ProductStatus.ACTIVE) {
			throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		ProductUpdate(id, requestDto);
		return new ProductUpdateResponseDto(product);
	}

	@Transactional
	@Override
	public ProductDeleteResponseDto deleteProduct(Long id, Long userId) {
		ProductEntity product = productRepository.findByIdOrElseThrow(id);
		UserEntity user = userRepository.findByIdOrElseThrow(userId);

		product.setStatus(ProductStatus.INACTIVE);

		// 이미 삭제된 상품인지 확인 (`status`가 `INACTIVE`이면 삭제 불가)
		if (product.getStatus() == ProductStatus.INACTIVE || product.getIsDeleted()) {
			throw new DuplicateException(PRODUCT_ISDELETED);
		}

		// 삭제 권한 체크: 본인이 등록한 상품이거나, 관리자인 경우 삭제 가능
		if (!product.getUser().getId().equals(user.getId()) && !user.getRole().equals(UserRole.ADMIN)) {
			throw new DuplicateException(FORBIDDEN_ACCESS);
		}

		return new ProductDeleteResponseDto(id, PRODUCT_DELETE);
	}

	@Transactional(timeout = 5, rollbackFor = Exception.class)
	public void ProductUpdate(Long productId, ProductUpdateRequestDto requestDto) {
		ProductEntity product = getFindByIdWithLock(productId);
		product.update(requestDto);
		productRepository.save(product);
	}

	public ProductEntity getFindByIdWithLock(Long productId) {
		return productRepository.findByIdWithLock(productId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	public ProductEntity findById(Long productId) {
		return productRepository.findByIdOrElseThrow(productId);
	}

}
