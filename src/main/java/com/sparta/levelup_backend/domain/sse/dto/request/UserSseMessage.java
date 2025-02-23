package com.sparta.levelup_backend.domain.sse.dto.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.levelup_backend.domain.sse.entity.SseMessageEntity;

public class UserSseMessage {
	public static final String USER_CHANGED_MESSAGE = "유저 정보가 변경되었습니다.";
	public static final String USER_PASSWORD_CHANGED_MESSAGE = "유저 비밀번호가 변경되었습니다.";

	public static String jsonConverter(SseMessageEntity userSseMessageEntity) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.writeValueAsString(userSseMessageEntity);
	}
}
