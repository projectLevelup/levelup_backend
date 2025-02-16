package com.sparta.levelup_backend.domain.chat.service;

import java.util.List;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomListResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;

public interface ChatroomService {

	ChatroomResponseDto createChatroom(Long userId, Long targetUserId, String title);

	void leaveChatroom(Long id, Long chatroomId);

	List<ChatroomListResponseDto> findChatrooms(Long id);
}
