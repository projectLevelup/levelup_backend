package com.sparta.levelup_backend.domain.product.entity;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.exception.common.ProductOutOfAmount;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductRequestDto;
import com.sparta.levelup_backend.utill.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@NoArgsConstructor  // 기본 생성자 자동 생성
@AllArgsConstructor(access = AccessLevel.PROTECTED)  // 객체 생성을 제한 (생성자는 Builder 패턴 활용)
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY) // 이거 쓰지 마라 queryqsl을 써야한다.
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(nullable = false, length = 1000)
    private String contents;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = true)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = true)
    private String imgUrl;

    public ProductEntity(ProductCreateRequestDto dto, UserEntity user, GameEntity game) {
        this.user = user;
        this.game = game;
        this.productName = dto.getProductName();
        this.contents = dto.getContents();
        this.price = dto.getPrice();
        this.amount = dto.getAmount();
        this.status = dto.getStatus();
        this.imgUrl = dto.getImgUrl();
    }


    public void decreaseAmount() {
        if (this.amount <= 0) {
            throw new ProductOutOfAmount();
        }
        this.amount = this.amount - 1;
    }

    public void increaseAmount() {
        this.amount = this.amount + 1;
    }

    public void update(ProductUpdateRequestDto requestDto) {
        if (requestDto.getProductName() != null) {
            this.productName = requestDto.getProductName();
        }
        if (requestDto.getContents() != null) {
            this.contents = requestDto.getContents();
        }
        if (requestDto.getPrice() != null) {
            this.price = requestDto.getPrice();
        }
        if (requestDto.getAmount() != null) {
            this.amount = requestDto.getAmount();
        }
        if (requestDto.getStatus() != null) {
            this.status = requestDto.getStatus();
        }
    }

}
