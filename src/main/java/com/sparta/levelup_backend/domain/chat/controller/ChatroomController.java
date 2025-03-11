package com.sparta.levelup_backend.domain.chat.controller;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatroomCreateResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatroomListResponseDto;
import com.sparta.levelup_backend.domain.chat.service.ChatroomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatroomController {

	private final ChatroomService chatroomService;

	/**
	 * 채팅방 생생 API
	 * @param targetUserId 참가대상 유저 ID
	 */
	@PostMapping
	public ApiResponse<ChatroomCreateResponseDto> createChatroom(
		@AuthenticationPrincipal CustomUserDetails authUser,
		@RequestParam Long targetUserId,
		@RequestParam(required = false) String title
	) {
		return success(CREATED, CHATROOM_CREATE, chatroomService.createChatroom(authUser.getId(), targetUserId, title));
	}

	/**
	 * 채팅방 나가기 API
	 */
	@DeleteMapping("/{chatroomId}")
	public ApiResponse<Void> leaveChatroom(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable String chatroomId) {
		chatroomService.leaveChatroom(authUser.getId(), chatroomId);
		return success(OK, CHATROOM_LEAVE);
	}

	/**
	 * 채팅방 목록 API
	 */
	@GetMapping
	public ApiResponse<Slice<ChatroomListResponseDto>> findChatrooms(
		@AuthenticationPrincipal CustomUserDetails authUser,
		@PageableDefault Pageable pageable
	) {
		return success(OK, CHATROOM_FIND ,chatroomService.findChatrooms(authUser.getId(), pageable));
	}

}
