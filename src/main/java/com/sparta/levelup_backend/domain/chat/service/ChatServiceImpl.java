package com.sparta.levelup_backend.domain.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.document.ChatMessage;
import com.sparta.levelup_backend.domain.chat.dto.request.ChatRequestDto;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatResponseDto;
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

	private static final String MIDNIGHT = "0 0 0 * * ?";
	private static final String EVERY_HOUR = "0 0 * * * ?";
	public static final String REDIS_CHATROOM_KEY = "chatroom:";
	private static final String NOT_SAVING_MESSAGES = "Not saving any messages";
	private static final String SUCCESS_SAVED_MESSAGES = "Successfully saved messages: {}, key: {}";
	private static final String FAILED_REDIS_SAVE = "Failed redis saved: {}";

	/**
	 * Redis에 메시지를 기록 후 Redis Pub/Sub으로 발행합니다.
	 */
	@Override
	public ChatResponseDto handleMessage(String chatroomId, ChatRequestDto dto, Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		ChatMessage chatMessage = ChatMessage.of(chatroomId, user.getId(), user.getNickName(), dto.getMessage());

		saveMessageToRedis(chatroomId, chatMessage);

		ChatResponseDto message = ChatResponseDto.from(chatMessage);
		redisPublisher.publish(getTopic(chatroomId), message);

		return message;
	}

	/**
	 * 채팅 메시지 기록을 조회합니다.
	 * Redis 데이터가 충분하다면 Redis로 조회
	 * Redis 데이터가 없을때 MongoDB로 조회
	 */
	@Override
	public Slice<ChatResponseDto> findChatHistory(String chatroomId, Pageable pageable) {
		String redisKey = REDIS_CHATROOM_KEY + chatroomId;
		Long cachedCount = redisTemplateMessage.opsForList().size(redisKey);
		int requiredCount = pageable.getPageSize() * (pageable.getPageNumber());

		if (cachedCount != null && cachedCount > 0 && cachedCount >= requiredCount) {
			return findMessagesToRedis(pageable, redisKey, cachedCount);
		}

		return findMessagesToMongoDB(chatroomId, pageable, cachedCount);
	}

	/**
	 * 매일 한시간마다 Redis에 기록된 메시지를 MongoDB에 저장합니다.
	 */
	@Scheduled(cron = EVERY_HOUR)
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

	/**
	 * 메시지 발행시 Redis에 기록합니다.
	 */
	private void saveMessageToRedis(String chatroomId, ChatMessage chatMessage) {
		try {
			redisTemplateMessage.opsForList().rightPush(REDIS_CHATROOM_KEY + chatroomId, chatMessage);
		} catch (Exception e) {
			log.error(FAILED_REDIS_SAVE, e.getMessage(), e);
		}
	}

	/**
	 * MongoDB로 메시지 기록 조회
	 * Redis로 조회한 페이지만큼 재조정하고 조회합니다.
	 */
	private SliceImpl<ChatResponseDto> findMessagesToMongoDB(String chatroomId, Pageable pageable, Long cachedCount) {
		Pageable mongoPageable = getPageable(pageable, cachedCount);

		Slice<ChatMessage> messages = chatMongoRepository.findMessagesByChatroomIdOrderByIdDesc(chatroomId, mongoPageable);
		List<ChatResponseDto> result = messages.getContent().stream()
			.map(ChatResponseDto::from)
			.collect(Collectors.toList());
		return new SliceImpl<>(result, pageable, messages.hasNext());
	}

	/**
	 * Redis로 메시지 기록 조회
	 */
	private SliceImpl<ChatResponseDto> findMessagesToRedis(Pageable pageable, String redisKey, Long cachedCount) {
		int total = cachedCount.intValue();
		int pageSize = pageable.getPageSize();
		int pageNumber = pageable.getPageNumber();

		int end = total - (pageNumber * pageSize) - 1;
		int start = Math.max(end - pageSize + 1, 0);
		List<ChatMessage> messages = redisTemplateMessage.opsForList().range(redisKey, start, end);

		Collections.reverse(messages);

		List<ChatResponseDto> result = messages.stream()
			.map(ChatResponseDto::from)
			.collect(Collectors.toList());
		boolean hasNext = (cachedCount > end + 1);
		return new SliceImpl<>(result, pageable, hasNext);
	}

	/**
	 * 생성한 토픽을 반환합니다.
	 */
	private ChannelTopic getTopic(String chatroomId) {
		return new ChannelTopic(REDIS_CHATROOM_KEY + chatroomId);
	}

	private static Pageable getPageable(Pageable pageable, Long cachedCount) {
		int mongoPage = (cachedCount == 0) ? 0 : (int)(((cachedCount - 1) / pageable.getPageSize()) + 1);
		Pageable mongoPageable = PageRequest.of(pageable.getPageNumber() - mongoPage, pageable.getPageSize(), pageable.getSort());
		return mongoPageable;
	}
}
