package com.sparta.levelup_backend.domain.chat.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatroomListResponseDto {

	private final Long chatroomId;
	private final String nickname;
	private final String ProfileImgUrl;

}
