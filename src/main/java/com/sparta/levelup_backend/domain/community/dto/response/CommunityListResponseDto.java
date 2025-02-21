package com.sparta.levelup_backend.domain.community.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityListResponseDto {
	private final List<CommunityReadResponseDto> communityList;

}
