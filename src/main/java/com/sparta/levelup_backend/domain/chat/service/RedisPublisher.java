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

	public void publish(ChannelTopic topic, ChatMessageDto dto) {
		redisTemplate.convertAndSend(topic.getTopic(), dto);
	}
}
