package com.sparta.levelup_backend.ui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomParticipantRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final ChatroomParticipantRepository cpRepository;

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	// 메인 페이지
	@GetMapping("/")
	public String mainPage() {
		return "main";
	}

	// 메인 페이지
	@GetMapping("/chat-main")
	public String chatMainPage() {
		return "chatMain";
	}

	@GetMapping("/signup")
	public String signupPage() {
		return "signup";
	}

	// 채팅방 페이지
	@GetMapping("/chatroom")
	public String getChatroomPage(@RequestParam Long chatroomId,
								  @AuthenticationPrincipal CustomUserDetails authUser,
								  Model model,
								  RedirectAttributes redirectAttributes) {
		// 현재 사용자가 해당 채팅방의 참가자인지 확인
		if (!cpRepository.existsByUserIdAndChatroomId(authUser.getId(), chatroomId)) {
			redirectAttributes.addFlashAttribute("errorMessage", "참여하지 않은 채팅방에 접근하실 수 없습니다.");
			return "redirect:/";
		}

		model.addAttribute("chatroomId", chatroomId);
		model.addAttribute("nickname", authUser.getUser().getNickName());
		return "chatroom";
	}

	// 채팅방 목록 페이지
	@GetMapping("/chatroomList")
	public String chatroomList() {
		return "chatroomList";
	}
}
