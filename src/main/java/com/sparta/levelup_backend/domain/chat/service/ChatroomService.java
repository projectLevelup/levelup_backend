package com.sparta.levelup_backend.domain.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomCreateResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomListResponseDto;

public interface ChatroomService {

	ChatroomCreateResponseDto createChatroom(Long userId, Long targetUserId, String title);

	void leaveChatroom(Long id, String chatroomId);

	Slice<ChatroomListResponseDto> findChatrooms(Long id, Pageable pageable);

	void updateUnreadCountAndLastMessage(String chatroomId, Long publisherId, String Message);

	void updateUnreadCountZero(String chatroomId, Long publisherId);

}
