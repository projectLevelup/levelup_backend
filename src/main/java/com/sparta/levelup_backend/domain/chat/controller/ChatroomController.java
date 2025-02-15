package com.sparta.levelup_backend.domain.chat.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;
import com.sparta.levelup_backend.domain.chat.service.ChatroomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/chats")
@RequiredArgsConstructor
public class ChatroomController {

	private final ChatroomService chatroomService;

	/**
	 * 채팅방 생생 API
	 * @param targetUserId 참가대상 유저 ID
	 * @return
	 */
	@PostMapping
	public ApiResponse<ChatroomResponseDto> createChatroom(
		@AuthenticationPrincipal CustomUserDetails authUser,
		@RequestParam Long targetUserId,
		@RequestParam(defaultValue = "Default Title") String title
	) {
		return success(CREATED, CHATROOM_CREATE, chatroomService.createChatroom(authUser.getId(), targetUserId, title));
	}

	/**
	 * 채팅방 나가기 API
	 */
	@DeleteMapping("/{chatroomId}")
	public ApiResponse<Void> leaveChatroom(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long chatroomId) {
		chatroomService.leaveChatroom(authUser.getId(), chatroomId);
		return success(OK, CHATROOM_LEAVE);
	}

	/**
	 * 채팅방 목록 API
	 */
	@GetMapping
	public List<ChatroomResponseDto> findChatrooms(@AuthenticationPrincipal CustomUserDetails authUser) {
		return chatroomService.findChatrooms(authUser.getId());
	}

}
