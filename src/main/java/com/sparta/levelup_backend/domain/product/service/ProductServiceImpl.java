package com.sparta.levelup_backend.domain.product.service;

import static com.sparta.levelup_backend.common.ApiResMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repsitory.GameRepository;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;

import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;


    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllByIsDeletedFalse().stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        return productRepository.findByIdAndIsDeletedFalse(id)
            .map(ProductResponseDto::new)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "상품 ID: " + id));
    }

    @Transactional
    @Override
    public ProductCreateResponseDto productCreate(ProductCreateRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, "사용자 ID: " + dto.getUserId()));

        GameEntity game = gameRepository.findById(dto.getGameId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.GAME_NOT_FOUND, "게임 ID: " + dto.getGameId()));

        ProductEntity product = new ProductEntity(dto, user, game);
        ProductEntity savedProduct = productRepository.save(product);

        return new ProductCreateResponseDto(
            savedProduct
        );
    }

    @Transactional
    @Override
    public ProductUpdateResponseDto updateProduct(Long id, ProductUpdateRequestDto requestDto) {
        ProductEntity product = productRepository.findByIdOrElseThrow(id);
        product.update(requestDto);
        productRepository.save(product);
        return new ProductUpdateResponseDto(product);
    }
    @Transactional
    @Override
    public ProductDeleteResponseDto deleteProduct(Long id) {
        ProductEntity product = productRepository.findByIdOrElseThrow(id);
        product.Productdelete();
        return new ProductDeleteResponseDto(id,PRODUCT_DELETE);
    }

    @Transactional
    @Override
    public List<ProductEntity> getProductsByGameId(Long gameId) {
        return productRepository.findByGameId(gameId);
    }



    @Transactional(timeout = 5, rollbackFor = Exception.class)
    public void decreaseAmount(Long productId) {
        ProductEntity product = getFindByIdWithLock(productId);
        product.decreaseAmount();
        productRepository.save(product);
    }

    @Transactional(timeout = 5, rollbackFor = Exception.class)
    public void increaseAmount(Long productId) {
        ProductEntity product = getFindByIdWithLock(productId);
        product.increaseAmount();
        productRepository.save(product);
    }

    public ProductEntity getFindByIdWithLock(Long productId) {
        return productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public ProductEntity findById(Long productId) {
      return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private ProductResponseDto convertToDto(ProductEntity entity) {
        return new ProductResponseDto(
            entity.getUser().getId(),
            entity.getGame().getId(),
            entity.getProductName(),
            entity.getContents(),
            entity.getPrice(),
            entity.getAmount(),
            entity.getStatus(),
            entity.getImgUrl()
        );
    }
}
