package com.sparta.levelup_backend.domain.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.document.ChatMessage;
import com.sparta.levelup_backend.domain.chat.dto.ChatRequestDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatResponseDto;
import com.sparta.levelup_backend.domain.chat.repository.ChatMongoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatMongoRepository chatMongoRepository;
	private final RedisPublisher redisPublisher;
	private final RedisTemplate<String, ChatMessage> redisTemplateMessage;

	private static final String REDIS_CHATROOM_KEY = "chatroom:";

	@Override
	public ChatResponseDto handleMessage(String chatroomId, ChatRequestDto dto, Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		ChatResponseDto message = new ChatResponseDto(
			user.getId(),
			user.getNickName(),
			dto.getMessage()
		);

		ChatMessage chatMessage = ChatMessage.builder()
			.chatroomId(chatroomId)
			.userId(user.getId())
			.nickname(message.getNickname())
			.message(message.getMessage())
			.build();

		redisPublisher.publish(getTopic(chatroomId), message);
		redisTemplateMessage.opsForList().rightPush(REDIS_CHATROOM_KEY + chatroomId, chatMessage);

		return message;
	}

	@Override
	public Slice<ChatResponseDto> findChatHistory(String chatroomId, Pageable pageable) {
		Slice<ChatMessage> messages = chatMongoRepository.findByChatroomIdOrderByTimestampDesc(chatroomId, pageable);

		List<ChatResponseDto> dtos = messages.stream()
			.map(msg -> new ChatResponseDto(
				msg.getUserId(),
				msg.getNickname(),
				msg.getMessage()))
			.collect(Collectors.toList());

		return new SliceImpl<>(dtos, pageable, messages.hasNext());
	}

	private ChannelTopic getTopic(String chatroomId) {
		return new ChannelTopic(REDIS_CHATROOM_KEY + chatroomId);
	}

	@Scheduled(cron = "0 */5 * * * ?")
	private void SaveMessage() {

		Set<String> keys = redisTemplateMessage.keys(REDIS_CHATROOM_KEY + "*");

		if (keys.isEmpty()) {
			log.info("Not saving any messages");
			return;
		}

		for (String key : keys) {

			List<ChatMessage> cachedMessages = redisTemplateMessage.opsForList().range(key, 0, -1);

			if (cachedMessages != null && !cachedMessages.isEmpty()) {
				chatMongoRepository.saveAll(cachedMessages);
				log.info("Successfully saved messages: {}, key: {}", cachedMessages.size(), key);
			}

			redisTemplateMessage.delete(key);
		}
	}

}
