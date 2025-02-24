package com.sparta.levelup_backend.domain.sse.service;

import static com.sparta.levelup_backend.domain.sse.dto.AlertMessage.*;
import static com.sparta.levelup_backend.domain.sse.dto.request.AlertMessageDto.*;
import static com.sparta.levelup_backend.exception.common.ErrorCode.*;
import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.levelup_backend.domain.sse.dto.response.AlertLogResponseDto;
import com.sparta.levelup_backend.domain.sse.entity.AlertMessageEntity;
import com.sparta.levelup_backend.domain.sse.entity.AlertMessageLogEntity;
import com.sparta.levelup_backend.domain.sse.repository.AlertMessageLogRepository;
import com.sparta.levelup_backend.domain.sse.repository.AlertMessageRepository;
import com.sparta.levelup_backend.domain.sse.repository.AlertRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.NotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlertServiceImpl implements AlertService {

	private final AlertMessageRepository alertMessageRepository;
	private final AlertRepository alertRepository;
	private final UserRepository userRepository;
	private final AlertMessageLogRepository alertMessageLogRepository;

	@Override
	public SseEmitter alertSubscribe(Long userId, String lastEventId) {

		List<String> alerts = alertRepository.findAllAlertById(userId.toString());
		SseEmitter alert = new SseEmitter(60 * 60 * 1000L);
		String alertId = userId + "_" + System.currentTimeMillis();

		if (alerts.isEmpty()) {
			alertRepository.save(alertId, alert);
		} else {
			if (alerts.size() >= 3) {
				alertRepository.deleteById(alerts.remove(0));
				alertRepository.save(alertId, alert);
			} else {
				alertRepository.save(alertId, alert);
			}
		}

		if (!lastEventId.equals("")) {
			sendAlertMessage(alert, alertId, new AlertMessageEntity("sseCreated"));
			sendSseMessageExceedingId(alert, alertId, Long.parseLong(lastEventId), userId);
		} else {
			sendAlertMessage(alert, alertId, new AlertMessageEntity("sseCreated"));
		}

		alert.onCompletion(() -> alertRepository.deleteById(alertId));
		alert.onTimeout(() -> alertRepository.deleteById(alertId));

		return alert;

	}

	@Override
	public void sendAlertMessage(SseEmitter alert, String alertId, AlertMessageEntity alertMessageEntity) {

		try {
			String jsonMessage = jsonConverter(alertMessageEntity);
			alert.send(event()
				.name("ALERT")
				.id(alertId)
				.data(jsonMessage));
		} catch (Exception e) {
			alert.completeWithError(e);
			alertRepository.deleteById(alertId);
		}
	}

	@Override
	public void readAllAlert(Long userId) {

		List<AlertMessageEntity> readTargetAlertMessages = alertMessageRepository.findByIdAndRange(userId, 0L, -1L);
		alertMessageRepository.deleteById(userId, readTargetAlertMessages);

	}

	@Override
	public void readAlert(Long userId, Long alertId) {

		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		List<AlertMessageEntity> readTargetAlertMessage = alertMessageRepository.findById(userId, alertId);
		alertMessageRepository.deleteById(userId, readTargetAlertMessage);

	}

	@Override
	public Page<AlertLogResponseDto> findLogByuserId(Long userId, Pageable pageable) {
		return alertMessageLogRepository.findAllByUserId(userId, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendAlertMessage(Long userId, AlertMessageEntity sseMessage, Long logId) {

		List<String> sses = alertRepository.findAllAlertById(userId.toString());

		for (String sse : sses) {
			SseEmitter alert = alertRepository.findById(sse);
			sendAlertMessage(alert, sse, sseMessage);
		}
		if (logId != 0 && !sses.isEmpty()) {
			AlertMessageLogEntity log = alertMessageLogRepository.findByIdOrElseThrow(logId);
			log.sended();
		}

	}

	private void sendSseMessageExceedingId(SseEmitter alert, String emitterId, Long alertId, Long userId) {

		List<AlertMessageEntity> alertMessages = alertMessageRepository.findByIdAndRange(userId, alertId, -1L);
		String jsonMessage = "";

		if (alertMessages.isEmpty()) {
			new NotFoundException(ALERT_MESSAGE_NOT_FOUND);
		}

		try {
			for (AlertMessageEntity alertMessageData : alertMessages) {
				jsonMessage = jsonMessage + jsonConverter(alertMessageData);
			}
			if (jsonMessage.equals("")) {
				jsonMessage = ALREADY_MARKED_READ;
			}
			alert.send(event()
				.name("ALERT")
				.id(String.valueOf(userId))
				.data(jsonMessage));
		} catch (Exception e) {
			alert.completeWithError(e);
			alertRepository.deleteById(emitterId);
		}

	}
}
