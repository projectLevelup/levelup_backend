package com.sparta.levelup_backend.domain.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.listener.ChannelTopic;
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
	private final RedisPublisher redisPublisher;

	@Override
	public ChatMessageDto handleMessage(Long chatroomId, ChatMessageDto dto, Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		ChatMessageDto messageDto = new ChatMessageDto(
			chatroomId,
			user.getUser().getNickName(),
			dto.getMessage()
		);

		ChatMessage chatMessage = new ChatMessage(
			chatroomId,
			messageDto.getNickname(),
			messageDto.getMessage(),
			LocalDateTime.now()
		);

		chatMongoRepository.save(chatMessage);
		redisPublisher.publish(getTopic(chatroomId), messageDto);
		return messageDto;
	}

	@Override
	public List<ChatMessageDto> findChatHistory(Long chatroomId) {
		List<ChatMessage> messages = chatMongoRepository.findByChatroomId(chatroomId);
		return messages.stream()
			.map(msg -> {
				ChatMessageDto dto = new ChatMessageDto(
					chatroomId,
					msg.getNickname(),
					msg.getMessage()
				);
				return dto;
			})
			.collect(Collectors.toList());
	}

	public ChannelTopic getTopic(Long chatroomId) {
		return new ChannelTopic("chatroom:" + chatroomId);
	}
}
