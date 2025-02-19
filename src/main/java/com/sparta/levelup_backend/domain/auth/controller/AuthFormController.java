package com.sparta.levelup_backend.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/v2")
public class AuthFormController {

	@GetMapping("/signin")
	public String signInPage(HttpServletRequest request) {

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

}
