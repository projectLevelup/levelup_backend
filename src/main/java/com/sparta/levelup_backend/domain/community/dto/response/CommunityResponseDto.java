package com.sparta.levelup_backend.domain.community.dto.response;

import com.sparta.levelup_backend.domain.community.document.CommunityDocument;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityResponseDto {
	private final Long communityId;
	private final String title;
	private final String content;
	private final String author; //글을 생성한 사용자의 email
	private final String game; // 글이 포함된 game의 name;

	public static CommunityResponseDto from(CommunityEntity community) {
		return new CommunityResponseDto(community.getId(), community.getTitle(), community.getContent(), community.getUser().getEmail(),
			community.getGame().getName());
	}

	public static CommunityResponseDto from(CommunityDocument communityDocument) {
		return new CommunityResponseDto(communityDocument.getUserId(), communityDocument.getTitle(), communityDocument.getContent(),
			communityDocument.getUserEmail(), communityDocument.getGameName());
	}

	public static CommunityResponseDto of(CommunityEntity community, UserEntity user, GameEntity game) {
		return new CommunityResponseDto(community.getId(), community.getTitle(), community.getContent(), user.getEmail(), game.getName());
	}
}
