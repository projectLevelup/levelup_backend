package com.sparta.levelup_backend.domain.chat.service;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;

public interface ChatroomService {

	public ChatroomResponseDto createChatroom(Long userId, String title);

	ChatroomResponseDto createPrivateChatroom(Long id, Long targetUserId, String title);

	Boolean leaveChatroom(Long id, Long chatroomId);
}
