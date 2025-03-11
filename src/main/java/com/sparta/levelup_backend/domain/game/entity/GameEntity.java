package com.sparta.levelup_backend.domain.game.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Entity
@Getter
@Builder
@Table(name = "game", indexes = {@Index(name = "idx_game_name", columnList = "name")})
@NoArgsConstructor
@AllArgsConstructor( access = PROTECTED)
public class GameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String genre;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void updateName(String name){
        this.name = name;
    }
    public void updateImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    public void updateGenre(String genre){
        this.genre = genre;
    }

    public void deleteGame(){
        this.delete();
    }

}
