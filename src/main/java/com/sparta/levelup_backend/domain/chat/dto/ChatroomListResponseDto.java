package com.sparta.levelup_backend.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatroomListResponseDto {

	private final String chatroomId;
	private final String nickname;
	private final String ProfileImgUrl;
	private final String lastMessage;
	private final Integer unreadMessageCount;

}
