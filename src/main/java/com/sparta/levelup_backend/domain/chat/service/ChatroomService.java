package com.sparta.levelup_backend.domain.chat.service;

import java.util.List;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;

public interface ChatroomService {

	ChatroomResponseDto createChatroom(Long userId, Long targetUserId, String title);

	Boolean leaveChatroom(Long id, Long chatroomId);

	List<ChatroomResponseDto> findChatrooms(Long id);
}
