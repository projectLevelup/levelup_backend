package com.sparta.levelup_backend.domain.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.levelup_backend.domain.community.document.CommunityDocument;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.Getter;

@Getter
public class CommunityReadResponseDto {
	private final String communityId;
	private final String title;
	private final String author; //글을 생성한 사용자의 nickname
	private final String game; // 글이 포함된 game의 name;

	@QueryProjection
	public CommunityReadResponseDto(String communityId, String title, String author, String game){
		this.communityId = communityId;
		this.title = title;
		this.author = author;
		this.game = game;
	}

	public static CommunityReadResponseDto from(CommunityDocument communityDocument) {
		return new CommunityReadResponseDto(communityDocument.getId(), communityDocument.getTitle(),
			communityDocument.getUserNickName(), communityDocument.getGameName());
	}

	public static CommunityReadResponseDto of(CommunityEntity community, UserEntity user, GameEntity game) {
		return new CommunityReadResponseDto(String.valueOf(community.getId()), community.getTitle(), user.getNickName(),
			game.getName());
	}
}