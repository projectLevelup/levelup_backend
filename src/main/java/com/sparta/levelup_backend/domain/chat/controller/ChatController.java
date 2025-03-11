package com.sparta.levelup_backend.domain.chat.controller;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.domain.chat.dto.request.ChatRequestDto;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatResponseDto;
import com.sparta.levelup_backend.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	/**
	 * WebSocket 메시지 처리
	 * 메시지 수신 후 -> 구독자에게 전달
	 */
	@MessageMapping("/chats/{chatroomId}") // 메시지 전송 endpoint
	public ChatResponseDto handleMessage(
		@DestinationVariable String chatroomId,
		@Payload ChatRequestDto dto,
		Authentication authentication
	) {
		return chatService.handleMessage(chatroomId, dto, authentication);
	}

	/**
	 * 메시지 기록 조회 API
	 */
	@GetMapping("/chats/{chatroomId}/history")
	public ApiResponse<Slice<ChatResponseDto>> findChatHistory(
		@PathVariable String chatroomId,
		@PageableDefault Pageable pageable
	) {
		return success(OK, MESSAGE_SAVE_SUCCESS, chatService.findChatHistory(chatroomId, pageable));
	}
}
