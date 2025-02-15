package com.sparta.levelup_backend.domain.chat.Controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.dto.ChatMessageDto;

@RestController
public class ChatController {

	@MessageMapping("/chats/{chatroomId}")
	@SendTo("/sub/chats/{chatroomId}")
	public ChatMessageDto handleMessage(
		@DestinationVariable String chatroomId,
		@Payload ChatMessageDto dto,
		Authentication authentication
	) {

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		dto.setNickname(user.getUser().getNickName());
		return dto;
	}
}
