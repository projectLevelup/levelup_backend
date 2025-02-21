package com.sparta.levelup_backend.domain.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 메시지 발행 메서드
	 * @param topic 발행할 채널(Topic)
	 * @param dto 채널ID, nickname, message
	 */
	public void publish(ChannelTopic topic, ChatMessageDto dto) {
		redisTemplate.convertAndSend(topic.getTopic(), dto);
	}
}
