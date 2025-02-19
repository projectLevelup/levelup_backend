package com.sparta.levelup_backend.domain.product.entity;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.exception.common.ProductOutOfAmount;
import com.sparta.levelup_backend.utill.ProductStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Document(indexName = "products")
public class ProductEntity extends BaseEntity {

	@Getter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id", nullable = false)
	private GameEntity game;

	@Column(nullable = false, length = 255)
	private String productName;

	@Column(nullable = false, length = 1000)
	@Field(type = FieldType.Text, analyzer = "standard")
	private String contents;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private Integer amount;

	@Enumerated(EnumType.STRING)
	@Field(type = FieldType.Keyword)
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

	public void update(ProductUpdateRequestDto dto) {
		this.productName = dto.getProductName();
		this.contents = dto.getContents();
		this.price = dto.getPrice();
		this.amount = dto.getAmount();
		this.status = ProductStatus.valueOf(dto.getStatus());
		this.imgUrl = dto.getImgUrl();
	}

	public void decreaseAmount() {
		if (this.amount == null || this.amount <= 0) {
			throw new ProductOutOfAmount();
		}
		this.amount -= 1;
	}

	public void increaseAmount() {
		if (this.amount != null) {
			this.amount += 1;
		}
	}
}
