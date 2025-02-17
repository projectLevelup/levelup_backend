package com.sparta.levelup_backend.domain.community.dto.response;

import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityReadResponseDto {
	private final String title;
	private final String author; //글을 생성한 사용자의 email
	private final String game; // 글이 포함된 game의 name;

	public static CommunityReadResponseDto of(CommunityEntity community, UserEntity user, GameEntity game) {
		return new CommunityReadResponseDto(community.getTitle(), user.getEmail(), game.getName());
	}
}
