package com.sparta.levelup_backend.domain.community.controller;

import static com.sparta.levelup_backend.common.apiResponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiResponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.apiResponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityCommentResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.service.CommunityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
	private final CommunityService communityService;

	// community 생성
	@PostMapping
	public ApiResponse<CommunityResponseDto> saveCommunity(
		@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommnunityCreateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.saveCommunity(userId, dto);
		return success(OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

	/**
	 *게임생활 목록 조회
	 * @param pageable 0부터 시작
	 * @param gameName 어떤 게임의 게임생활을 조회할건지
	 * @return
	 */
	@GetMapping
	public ApiResponse<CommunityListResponseDto> findAllCommunityByGameName(@RequestParam String gameName,
		@PageableDefault(size = 10) Pageable pageable) {

		CommunityListResponseDto responseDtoList = communityService.findAllByGameName(gameName, pageable);

		return success(OK, COMMUNITY_LIST_FOUND_SUCCESS, responseDtoList);
	}

	// 게임생활 목록 검색
	@GetMapping("/search")
	public ApiResponse<CommunityListResponseDto> findCommunitiesBySearchKeyword(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size, @RequestParam String searchKeyword,
		@RequestParam String gameName) {

		CommunityListResponseDto responseDtoList = communityService.findCommunities(searchKeyword, gameName, page,
			size);
		return success(OK, COMMUNITY_LIST_FOUND_SUCCESS, responseDtoList);
	}

	// 게임생활 단건 조회(+ 댓글 조회)
	@GetMapping("/{communityId}")
	public ApiResponse<CommunityCommentResponseDto> findCommunity(@PathVariable Long communityId) {
		CommunityCommentResponseDto responseDto = communityService.findById(communityId);

		return success(OK, COMMUNITY_FOUND_SUCCESS, responseDto);
	}

	// community 수정
	@PatchMapping
	public ApiResponse<CommunityResponseDto> updateCommunity(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommunityUpdateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.updateCommunity(userId, dto);
		return success(OK, COMMUNITY_UPDATE_SUCCESS, responseDto);
	}

	// community 삭제
	@DeleteMapping
	public ApiResponse<Void> deleteCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam Long communityId) {

		Long userId = customUserDetails.getId();

		communityService.deleteCommunity(userId, communityId);
		return success(OK, COMMUNITY_DELETE_SUCCESS);
	}
}