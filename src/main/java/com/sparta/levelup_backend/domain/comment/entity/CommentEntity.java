package com.sparta.levelup_backend.domain.comment.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "community_id")
	private CommunityEntity community;

	public CommentEntity(String content, UserEntity user, CommunityEntity community) {
		this.content = content;
		this.user = user;
		this.community = community;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void deleteComment() {
		this.delete();
	}
}
