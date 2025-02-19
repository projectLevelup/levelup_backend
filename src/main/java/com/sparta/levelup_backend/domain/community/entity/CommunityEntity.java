package com.sparta.levelup_backend.domain.community.entity;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "community")
public class CommunityEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Integer recommendation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id")
	private GameEntity game;

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void deleteCommunity() {
		this.delete();
	}

	public CommunityEntity(String title, String content, UserEntity user, GameEntity game) {
		this.title = title;
		this.content = content;
		this.user = user;
		this.game = game;
		recommendation = 0;
	}

}
