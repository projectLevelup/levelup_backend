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
	public static final String NOT_SAVING_MESSAGES = "Not saving any messages";
	public static final String SUCCESS_SAVED_MESSAGES = "Successfully saved messages: {}, key: {}";

	/**
	 * Redis에 메시지를 기록 후 Redis Pub/Sub으로 발행합니다.
	 */
	@Override
	public ChatResponseDto handleMessage(String chatroomId, ChatRequestDto dto, Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		ChatMessage chatMessage = ChatMessage.builder()
			.chatroomId(chatroomId)
			.userId(user.getId())
			.nickname(user.getNickName())
			.message(dto.getMessage())
			.build();

		redisTemplateMessage.opsForList().rightPush(REDIS_CHATROOM_KEY + chatroomId, chatMessage);

		ChatResponseDto message = ChatResponseDto.from(chatMessage);
		redisPublisher.publish(getTopic(chatroomId), message);

		return message;
	}

	/**
	 * 채팅 메시지 기록을 조회합니다.
	 * Redis 채팅 데이터 10개이상일 시 Redis 로 반환 / 그 외 MongoDB 조회
	 */
	@Override
	public Slice<ChatResponseDto> findChatHistory(String chatroomId, Pageable pageable) {
		String redisKey = REDIS_CHATROOM_KEY + chatroomId;
		Long cachedCount = redisTemplateMessage.opsForList().size(redisKey);

		if (cachedCount != null && cachedCount >= 10) {
			return findChatHistoryByRedis(pageable, cachedCount, redisKey);
		}

		return findChatHistoryByDB(chatroomId, pageable);
	}

	/**
	 * Redis 채팅 기록 조회 메서드
	 */
	private Slice<ChatResponseDto> findChatHistoryByRedis(Pageable pageable, Long cachedCount, String redisKey) {
		int start = (int) pageable.getOffset();
		int end = start + pageable.getPageSize() - 1;

		List<ChatMessage> cachedMessages = redisTemplateMessage.opsForList().range(redisKey, start, end);

		boolean hasNext = (cachedCount > end + 1);

		List<ChatResponseDto> chatResponseDto = cachedMessages.stream()
			.map(ChatResponseDto::from)
			.collect(Collectors.toList());

		return new SliceImpl<>(chatResponseDto, pageable, hasNext);
	}

	/**
	 * MongoDB 채팅 기록 조회 메서드
	 */
	private Slice<ChatResponseDto> findChatHistoryByDB(String chatroomId, Pageable pageable) {
		Slice<ChatMessage> messages = chatMongoRepository.findByChatroomIdOrderByTimestampAsc(chatroomId, pageable);

		List<ChatResponseDto> chatResponseDto = messages.getContent().stream()
			.map(ChatResponseDto::from)
			.collect(Collectors.toList());

		return new SliceImpl<>(chatResponseDto, pageable, messages.hasNext());
	}

	/**
	 * 생성한 토픽을 반환합니다.
	 */
	private ChannelTopic getTopic(String chatroomId) {
		return new ChannelTopic(REDIS_CHATROOM_KEY + chatroomId);
	}

	/**
	 * 매일 자정 Redis에 기록된 메시지를 MongoDB에 저장합니다.
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	private void SaveMessage() {
		Set<String> keys = redisTemplateMessage.keys(REDIS_CHATROOM_KEY + "*");

		if (keys.isEmpty()) {
			log.info(NOT_SAVING_MESSAGES);
			return;
		}

		for (String key : keys) {
			List<ChatMessage> cachedMessages = redisTemplateMessage.opsForList().range(key, 0, -1);

			if (cachedMessages != null && !cachedMessages.isEmpty()) {
				chatMongoRepository.saveAll(cachedMessages);
				log.info(SUCCESS_SAVED_MESSAGES, cachedMessages.size(), key);
			}

			redisTemplateMessage.delete(key);
		}
	}

}
