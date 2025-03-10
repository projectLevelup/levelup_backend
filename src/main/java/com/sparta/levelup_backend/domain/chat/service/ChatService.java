package com.sparta.levelup_backend.domain.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;

import com.sparta.levelup_backend.domain.chat.dto.request.ChatRequestDto;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatResponseDto;

public interface ChatService {
	ChatResponseDto handleMessage(String chatroomId, ChatRequestDto dto, Authentication authentication);
	Slice<ChatResponseDto> findChatHistory(String chatroomId, Pageable pageable);
}

