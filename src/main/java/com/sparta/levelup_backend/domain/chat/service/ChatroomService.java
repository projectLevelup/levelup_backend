package com.sparta.levelup_backend.domain.chat.service;

import java.util.List;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomCreateResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomListResponseDto;

public interface ChatroomService {

	ChatroomCreateResponseDto createChatroom(Long userId, Long targetUserId, String title);

	void leaveChatroom(Long id, String chatroomId);

	List<ChatroomListResponseDto> findChatrooms(Long id);

	void updateUnreadCountAndLastMessage(String chatroomId, Long publisherId, String Message);

	void updateUnreadCountZero(String chatroomId, Long publisherId);

}
