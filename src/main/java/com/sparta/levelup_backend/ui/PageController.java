package com.sparta.levelup_backend.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	// 메인 페이지
	@GetMapping("/")
	public String index() {
		return "main";
	}

	@GetMapping("/signup")
	public String signupPage() {
		return "signup";
	}

	// 채팅방 페이지
	@GetMapping("/chatroom")
	public String chatroom() {
		return "chatroom";
	}

	// 채팅방 목록 페이지
	@GetMapping("/chatroomList")
	public String chatroomList() {
		return "chatroomList";
	}
}
