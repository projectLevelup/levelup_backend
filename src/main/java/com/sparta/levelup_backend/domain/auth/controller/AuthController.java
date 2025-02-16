package com.sparta.levelup_backend.domain.auth.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.SIGNUP_SUCCESS;
import static com.sparta.levelup_backend.common.ApiResponse.success;
import static org.springframework.http.HttpStatus.CREATED;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@GetMapping("/signup")
	public String signUpUserPage(){

		return "signup";
	}

	@PostMapping("/signup")
	public ApiResponse<Void> signUpUser(@Valid @RequestBody SignUpUserRequestDto dto) {
		authService.signUpUser(dto);
		return success(CREATED, SIGNUP_SUCCESS);
	}
}
