package com.sparta.levelup_backend.domain.chat.dto;

import com.sparta.levelup_backend.domain.chat.document.ChatroomDocument;
import com.sparta.levelup_backend.domain.chat.document.Participant;

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

	public static ChatroomListResponseDto from(ChatroomDocument chatroom, Long userId) {
		Participant other = chatroom.getParticipants().stream()
			.filter(p -> !p.getUserId().equals(userId))
			.findFirst()
			.orElse(null);
		String nickname = other != null ? other.getNickname() : "";
		String profileImgUrl = other != null ? other.getProfileImgUrl() : "";
		Integer unreadCount = chatroom.getUnreadMessages().getOrDefault(userId.toString(), 0);
		return ChatroomListResponseDto.builder()
			.chatroomId(chatroom.getId())
			.nickname(nickname)
			.ProfileImgUrl(profileImgUrl)
			.lastMessage(chatroom.getLastMessage())
			.unreadMessageCount(unreadCount)
			.build();
	}

}
