package com.sparta.levelup_backend.domain.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;
import com.sparta.levelup_backend.domain.chat.entity.ChatMessage;
import com.sparta.levelup_backend.domain.chat.repository.ChatMongoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatMongoRepository chatMongoRepository;

	@Override
	public ChatMessageDto handleMessage(Long chatroomId, ChatMessageDto dto, Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		dto.setNickname(user.getUser().getNickName());

		ChatMessage chatMessage = new ChatMessage(
			chatroomId,
			dto.getNickname(),
			dto.getMessage(),
			LocalDateTime.now()
		);

		chatMongoRepository.save(chatMessage);
		return dto;
	}

	@Override
	public List<ChatMessageDto> findChatHistory(Long chatroomId) {
		List<ChatMessage> messages = chatMongoRepository.findByChatroomId(chatroomId);
		return messages.stream()
			.map(msg -> {
				ChatMessageDto dto = ChatMessageDto.builder()
					.nickname(msg.getNickname())
					.message(msg.getMessage())
					.build();
				return dto;
			})
			.collect(Collectors.toList());
	}
}
