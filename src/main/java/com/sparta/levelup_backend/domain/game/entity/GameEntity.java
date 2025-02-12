package com.sparta.levelup_backend.domain.game.entity;


import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@Builder
@Table(name = "game")
@NoArgsConstructor
@AllArgsConstructor( access = AccessLevel.PROTECTED)
public class GameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
