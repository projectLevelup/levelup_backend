package com.sparta.levelup_backend.domain.community.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityUpdateRequestDto {
	private final Long id; //커뮤니티 글 id
	private final String title;
	private final String content;
}
