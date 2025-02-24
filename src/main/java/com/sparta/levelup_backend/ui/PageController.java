package com.sparta.levelup_backend.ui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomMongoRepository;
import com.sparta.levelup_backend.domain.chat.service.ChatroomService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final ChatroomMongoRepository chatroomMongoRepository;
	private final ChatroomService chatroomService;

	@GetMapping("/v2/signin")
	public String signInPage() {
		return "signin";
	}

	@GetMapping("/v2/signup")
	public String signUpUserPage() {
		return "signup";
	}

	@GetMapping("/v2/oauth2signup")
	public String oAuth2SignUpUserPage(Model model, HttpServletRequest request) {

		model.addAttribute("email", request.getAttribute("email"));
		model.addAttribute("nickName", request.getAttribute("nickName"));
		model.addAttribute("phoneNummber", request.getAttribute("phoneNumber"));

		return "oauth2signup";
	}

	@GetMapping("/v2/home")
	public String homePage() {

		return "home";
	}

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

	// 채팅방 페이지
	@GetMapping("/chatroom")
	public String getChatroomPage(@RequestParam String chatroomId,
		@AuthenticationPrincipal CustomUserDetails authUser,
		Model model,
		RedirectAttributes redirectAttributes) {
		// 현재 사용자가 해당 채팅방의 참가자인지 확인
		if (!chatroomMongoRepository.findByUserIdAndChatroomId(authUser.getId(), chatroomId).isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "참여하지 않은 채팅방에 접근하실 수 없습니다.");
		}

		// 채팅방 접속 시 안 읽음 수 0으로 초기화
		chatroomService.updateUnreadCountZero(chatroomId, authUser.getId());

		model.addAttribute("chatroomId", chatroomId);
		model.addAttribute("nickname", authUser.getUser().getNickName());
		return "chatroom";
	}

	// 채팅방 목록 페이지
	@GetMapping("/chatroomList")
	public String chatroomList() {
		return "chatroomList";
	}

	@GetMapping("/product-detail")
	public String productDetail(@RequestParam("productId") Long productId, Model model) {
		model.addAttribute("productId", productId);
		return "productDetail";
	}

	@GetMapping("/resetPassword")
	public String resetPassword() {
		return "resetPassword";
	}

	@GetMapping("/resetPasswordConfirm")
	public String resetPasswordConfirm() {
		return "resetPasswordConfirm";
	}
}
