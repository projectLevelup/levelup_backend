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

	private final RedisTemplate<String, Object> redisTemplate;
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 수신받은 메시지 역직렬화 -> ChatMessageDto 로 변환
	 * /sub/chats/{chatroomId} 채널로 전파
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		ChatMessageDto chatMessage = (ChatMessageDto) redisTemplate.getValueSerializer().deserialize(message.getBody());
		messagingTemplate.convertAndSend("/sub/chats/" + chatMessage.getChatroomId(), chatMessage);
	}
}
