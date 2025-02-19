package com.sparta.levelup_backend.domain.community.dto.request;

import static com.sparta.levelup_backend.domain.community.dto.CommunityValidMessage.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityUpdateRequestDto {
	@NotNull(message = COMMUNITY_ID_REQUIRED)
	private final Long communityId;
	private final String title;
	private final String content;
}
