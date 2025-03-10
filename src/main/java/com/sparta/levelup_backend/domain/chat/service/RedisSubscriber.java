package com.sparta.levelup_backend.domain.chat.service;

import static com.sparta.levelup_backend.domain.chat.service.ChatServiceImpl.*;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.chat.dto.response.ChatResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		ChatResponseDto chatMessage = (ChatResponseDto) redisTemplate.getValueSerializer().deserialize(message.getBody());
		String channel = new String(message.getChannel());

		messagingTemplate.convertAndSend("/sub/chats/" + channel.replace(REDIS_CHATROOM_KEY, ""), chatMessage);
	}
}
