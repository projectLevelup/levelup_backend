package com.sparta.levelup_backend.domain.sse.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class AlertRepository {

	private final Map<String, SseEmitter> userAlert = new ConcurrentHashMap<>();

	public void save(String emitterId, SseEmitter sse) {
		
		userAlert.put(emitterId, sse);
	}

	public List<String> findAllAlertById(String userId) {

		List<String> emitterIds = new ArrayList<>();
		userAlert.keySet().forEach(key -> {
			if (key.startsWith(userId + "_")) {
				emitterIds.add(key);
			}
		});

		return emitterIds;
	}

	public SseEmitter findById(String emitterId) {

		return userAlert.get(emitterId);
	}

	public void deleteById(String emitterId) {

		userAlert.remove(emitterId);
	}
}
