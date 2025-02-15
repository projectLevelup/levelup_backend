package com.sparta.levelup_backend.domain.chat.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	 * @param title 채팅방 제목 - 임시로 Default Title 로 입력되도록 설정 (추후 변경 예정)
	 */
	@PostMapping
	public ChatroomResponseDto createChatroom(
		@AuthenticationPrincipal CustomUserDetails authUser,
		@RequestParam(defaultValue = "Default Title") String title
	) {
		return chatroomService.createChatroom(authUser.getId(), title);
	}

	/**
	 * 1:1 채팅방 생성 API
	 * @param targetUserId
	 * @param title 채팅방 제목 - 임시로 개인 채팅 로 입력되도록 설정 (추후 상대방 닉네임으로 생성되도록 변경)
	 */
	@PostMapping("/private")
	public ChatroomResponseDto createPrivateChatroom(
		@AuthenticationPrincipal CustomUserDetails authUser,
		@RequestParam Long targetUserId,
		@RequestParam(defaultValue = "개인 채팅") String title
	) {
		return chatroomService.createPrivateChatroom(authUser.getId(), targetUserId, title);
	}

	/**
	 * 채팅방 나가기 API
	 */
	@DeleteMapping("/{chatroomId}")
	public Boolean leaveChatroom(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long chatroomId) {
		return chatroomService.leaveChatroom(authUser.getId(), chatroomId);
	}

}
