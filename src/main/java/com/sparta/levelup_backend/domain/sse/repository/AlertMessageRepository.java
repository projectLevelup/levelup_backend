package com.sparta.levelup_backend.domain.sse.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.sse.entity.AlertMessageEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AlertMessageRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public AlertMessageEntity save(Long userId, AlertMessageEntity alertMessageEntity) {

		ListOperations<String, Object> alertMessage = redisTemplate.opsForList();
		String key = "Alert_" + userId;
		alertMessage.leftPush(key, alertMessageEntity);
		alertMessage.trim(key, 0, 9);

		return alertMessageEntity;
	}

	public List<AlertMessageEntity> findByIdAndRange(Long userId, Long start, Long end) {

		ListOperations<String, Object> alertMessage = redisTemplate.opsForList();
		String key = "Alert_" + userId;
		List<Object> objectMessages = alertMessage.range(key, start, end);
		List<AlertMessageEntity> alertMessageEntitys = new ArrayList<>();

		for (Object objectMessage : objectMessages) {
			AlertMessageEntity messageEntity = (AlertMessageEntity)objectMessage;
			alertMessageEntitys.add(messageEntity);
		}

		return alertMessageEntitys;
	}

	public void deleteById(Long userId, List<AlertMessageEntity> alertMessageEntitys) {

		ListOperations<String, Object> alertMessage = redisTemplate.opsForList();
		String key = "Alert_" + userId;

		for (AlertMessageEntity alertMessageEntity : alertMessageEntitys) {
			alertMessage.remove(key, 1, alertMessageEntity);
		}
	}

	public List<AlertMessageEntity> findById(Long userId, Long alertId) {

		ListOperations<String, Object> alertMessage = redisTemplate.opsForList();
		String key = "Alert_" + userId;
		List<Object> objectMessages = alertMessage.range(key, alertId, alertId);
		List<AlertMessageEntity> alertMessageEntitys = new ArrayList<>();

		for (Object objectMessage : objectMessages) {
			AlertMessageEntity messageEntity = (AlertMessageEntity)objectMessage;
			alertMessageEntitys.add(messageEntity);
		}

		return alertMessageEntitys;
	}
}
