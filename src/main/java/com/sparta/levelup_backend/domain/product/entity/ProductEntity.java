package com.sparta.levelup_backend.domain.product.entity;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.exception.common.ProductOutOfAmount;
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

    // ✅ ManyToOne 관계 설정 (User 테이블과 조인)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // ✅ ManyToOne 관계 설정 (Game 테이블과 조인)
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Lob
    @Column(nullable = true)
    private String imgUrl;  // ✅ BLOB 대신 URL 저장 방식 (AWS S3 사용 고려)

    public void decreaseAmount() {
        if (this.amount <= 0) {
            throw new ProductOutOfAmount();
        }
        this.amount = this.amount - 1;
    }

    public void increaseAmount() {
        this.amount = this.amount + 1;
    }
}
