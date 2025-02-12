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

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
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
