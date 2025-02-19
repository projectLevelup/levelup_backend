package com.sparta.levelup_backend.domain.community.dto.request;

import static com.sparta.levelup_backend.domain.community.dto.CommunityValidMessage.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommnunityCreateRequestDto {
	@NotBlank(message = TITLE_REQUIRED)
	private final String title;
	@NotBlank(message = CONTENT_REQUIRED)
	private final String content;
	@NotNull(message = GAME_ID_REQUIRED)
	private final Long gameId;
}
