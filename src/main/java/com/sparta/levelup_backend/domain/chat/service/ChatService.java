package com.sparta.levelup_backend.domain.chat.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.sparta.levelup_backend.domain.chat.dto.ChatRequestDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatResponseDto;

public interface ChatService {
	ChatResponseDto handleMessage(String chatroomId, ChatRequestDto dto, Authentication authentication);
	List<ChatResponseDto> findChatHistory(String chatroomId);
}

