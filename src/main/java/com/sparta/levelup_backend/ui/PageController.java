package com.sparta.levelup_backend.ui;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomMongoRepository;
import com.sparta.levelup_backend.domain.chat.service.ChatroomService;
import com.sparta.levelup_backend.domain.order.dto.response.OrderResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final ChatroomMongoRepository chatroomMongoRepository;
	private final ChatroomService chatroomService;

	@GetMapping("/signin")
	public String signInPage() {
		return "signin";
	}

	@GetMapping("/signup")
	public String signUpUserPage() {
		return "signup";
	}

	@GetMapping("/oauth2signup")
	public String oAuth2SignUpUserPage(Model model, HttpServletRequest request) {

		model.addAttribute("email", request.getAttribute("email"));
		model.addAttribute("nickName", request.getAttribute("nickName"));
		model.addAttribute("phoneNummber", request.getAttribute("phoneNumber"));

		return "oauth2signup";
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

	@GetMapping("/community-detail")
	public String communityDetail(@RequestParam("communityId") Long communityId, Model model) {
		model.addAttribute("communityId", communityId);
		return "community/community-detail";
	}

	@GetMapping("/resetPassword")
	public String resetPassword() {
		return "resetPassword";
	}

	@GetMapping("/resetPasswordConfirm")
	public String resetPasswordConfirm() {
		return "resetPasswordConfirm";
	}

	@GetMapping("/admin/dashboard")
	public String adminPage() {
		return "admin";
	}

	@GetMapping("/admin/dashboard/game")
	public String gamePage() {
		return "admin/game";
	}

	@GetMapping("/admin/dashboard/game/add")
	public String gameAddPage() {
		return "admin/create-game";
	}

	@GetMapping("/products/add")
	public String addProductPage() {
		return "productCreate";
	}

	@GetMapping("/profile")
	public String profilePage() {
		return "profile";
	}

	@GetMapping("/search")
	public String searchPage(@RequestParam(value = "productName", required = false) String productName,
							 Model model) {
		model.addAttribute("productName", productName);
		return "product/product-list"; // search.html 템플릿을 렌더링
	}

	@GetMapping("/profile/order/student")
	public String orderStudentPage() {
		return "order/order-list";
	}

	@GetMapping("/profile/order/tutor")
	public String orderTutorPage() {
		return "order/order-list-tutor";
	}

	@GetMapping("/profile/order/student/order-detail")
	public String orderDetailPage(@RequestParam Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "order/order-detail"; // src/main/resources/templates/order-detail.html
	}

	@GetMapping("/profile/order/tutor/order-detail")
	public String orderDetailTutorPage(@RequestParam Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "order/order-detail-tutor"; // src/main/resources/templates/order-detail.html
	}


	@GetMapping("/checkout")
	public String checkoutPage(@RequestParam Long orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "payment/checkout";
	}

	@GetMapping("/profile/bills/student")
	public String billsPage() {
		return "order/bill-list";
	}

	@GetMapping("/profile/bills/tutor")
	public String billsTutorPage() {
		return "order/bill-list-tutor";
	}

	@GetMapping("/profile/update")
	public String updatePage() {
		return "user/update";
	}

	@GetMapping("/review/write")
	public String reviewWritePage(@RequestParam Long productId,
								  Model model) {

		model.addAttribute("productId", productId);
		return "review/create"; // review/write.html 템플릿 렌더링
	}

	@GetMapping("/community")
	public String communityPage() {
		return "community/community";
	}

	@GetMapping("/community/add")
	public String addCommunityPage() {
		return "community/write";
	}

}
