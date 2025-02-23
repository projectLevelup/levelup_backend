package com.sparta.levelup_backend.domain.sse.event.publisher;

import com.sparta.levelup_backend.domain.sse.entity.SseMessageEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SseEvent {

	UserEntity user;
	SseMessageEntity userSseMessageEntity;
}
