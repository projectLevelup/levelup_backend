package com.sparta.levelup_backend.domain.community.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.service.CommunityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/community")
@RequiredArgsConstructor
public class CommunityController {
	private final CommunityService communityService;

	@PostMapping
	public ApiResponse<CommunityResponseDto> SaveCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody CommnunityCreateRequestDto dto) {

		Long userId = customUserDetails.getId();
		CommunityResponseDto responseDto = communityService.saveCommunity(userId, dto);
		return ApiResponse.success(HttpStatus.OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

}
