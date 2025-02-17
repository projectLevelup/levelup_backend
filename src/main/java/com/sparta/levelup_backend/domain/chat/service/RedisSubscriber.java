package com.sparta.levelup_backend.domain.chat.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

	private RedisTemplate<String, Object> redisTemplate;
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		ChatMessageDto chatMessage = (ChatMessageDto) redisTemplate.getValueSerializer().deserialize(message.getBody());
		messagingTemplate.convertAndSend("/sub/chats/" + chatMessage.getChatroomId(), chatMessage);
	}
}
