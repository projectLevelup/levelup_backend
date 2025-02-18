package com.sparta.levelup_backend.domain.chat.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;
import com.sparta.levelup_backend.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	/**
	 * WebSocket 메시지 처리
	 * 메시지 수신 후 -> 구독자에게 전달
	 * @param chatroomId 채팅방 ID
	 * @param dto 메시지 전송 객체 (닉네임, 메시지 포함)
	 */
	@MessageMapping("/chats/{chatroomId}") // 메시지 전송 endpoint
	public ChatMessageDto handleMessage(
		@DestinationVariable Long chatroomId,
		@Payload ChatMessageDto dto,
		Authentication authentication
	) {
		return chatService.handleMessage(chatroomId, dto, authentication);
	}

	/**
	 * 메시지 기록 저장 API
	 * 저장소: Mongo DB
	 */
	@GetMapping("/v1/chats/{chatroomId}/history")
	public ApiResponse<List<ChatMessageDto>> findChatHistory(@PathVariable Long chatroomId) {
		return success(CREATED, MESSAGE_SAVE_SUCCESS, chatService.findChatHistory(chatroomId));
	}
}
