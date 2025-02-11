package com.sparta.levelup_backend.domain.auth.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<String>> signUpUser(@RequestBody SignUpUserRequestDto createUserRequestDto){
		authService.signUpUser(createUserRequestDto);
		return new ResponseEntity (ApiResponse.success (SIGNUP_SUCCESS), HttpStatus.CREATED);
	}
}
