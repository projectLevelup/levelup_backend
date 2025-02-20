package com.sparta.levelup_backend.domain.chat.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;

public interface ChatService {
	ChatMessageDto handleMessage(String chatroomId, ChatMessageDto dto, Authentication authentication);
	List<ChatMessageDto> findChatHistory(String chatroomId);
}

