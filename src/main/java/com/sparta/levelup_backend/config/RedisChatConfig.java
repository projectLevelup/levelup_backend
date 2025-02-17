package com.sparta.levelup_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;
import com.sparta.levelup_backend.domain.chat.service.RedisSubscriber;

@Configuration
public class RedisChatConfig {

	/**
	 * Redis 메시지 구성 설정
	 * @param redisConnectionFactory Redis 연결
	 * @param redisSubscriber 수신된 메시지 처리 서비스
	 */
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		RedisSubscriber redisSubscriber
	) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(redisSubscriber, new PatternTopic("chatroom:*"));
		return container;
	}

	/**
	 * Redis 기본 설정
	 * key: String 문자열
	 * value: ChatMessageDto
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
		return template;
	}

}
