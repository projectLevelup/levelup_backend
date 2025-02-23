package com.sparta.levelup_backend.domain.sse.service;

import static com.sparta.levelup_backend.domain.sse.dto.request.UserSseMessage.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.levelup_backend.domain.sse.entity.SseMessageEntity;
import com.sparta.levelup_backend.domain.sse.repository.SseMessageRepository;
import com.sparta.levelup_backend.domain.sse.repository.SseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SseServiceImpl implements SseService {
	private final SseMessageRepository sseMessageRepository;
	private final SseRepository sseRepository;

	@Override
	public SseEmitter sseSubscribe(Long id, String lastEventId) {
		List<String> sses = sseRepository.findAllSseById(id.toString());
		SseEmitter alert = new SseEmitter(60 * 60 * 1000L);
		String alertId = id + "_" + System.currentTimeMillis();
		if (sses.isEmpty()) {
			sseRepository.save(alertId, alert);
		} else {
			if (sses.size() >= 3) {
				sseRepository.deleteById(sses.remove(0));
				sseRepository.save(alertId, alert);
			} else {
				sseRepository.save(alertId, alert);
			}
		}

		if (!lastEventId.equals("")) {
			sendSseMessageExceedingId(alert, alertId, Long.parseLong(lastEventId));
		} else {
			sendSseMessage(alert, alertId, new SseMessageEntity(id, "sseCreated"));
		}

		String finalAlertId = alertId;
		alert.onCompletion(() -> sseRepository.deleteById(finalAlertId));
		alert.onTimeout(() -> sseRepository.deleteById(finalAlertId));

		return alert;
	}

	public void sendSseMessage(SseEmitter alert, String alertId, SseMessageEntity SseMessageEntity) {
		try {
			String jsonMessage = jsonConverter(SseMessageEntity);
			alert.send(SseEmitter.event()
				.name("userDataChanged")
				.id(String.valueOf(SseMessageEntity.getId()))
				.data(jsonMessage));
		} catch (Exception e) {
			alert.completeWithError(e);
			sseRepository.deleteById(alertId);
		}

	}

	private void sendSseMessageExceedingId(SseEmitter alert, String alertId, Long id) {

		SseMessageEntity sseMessage = sseMessageRepository.findByIdOrElseThrow(id);
		List<SseMessageEntity> sseMessages = sseMessageRepository.findAllByUserId(sseMessage.getUserId());
		for (SseMessageEntity sseMessageData : sseMessages) {
			if (sseMessageData.getId() <= id) {
				continue;
			} else {
				sendSseMessage(alert, alertId, sseMessageData);
			}
		}
	}

	public void sendSseMessage(Long userId, SseMessageEntity sseMessage) {
		List<String> sses = sseRepository.findAllSseById(userId.toString());
		for (String sse : sses) {
			SseEmitter alert = sseRepository.findById(sse);
			sendSseMessage(alert, sse, sseMessage);
		}

	}
}
