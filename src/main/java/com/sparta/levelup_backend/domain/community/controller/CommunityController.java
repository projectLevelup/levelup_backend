package com.sparta.levelup_backend.domain.community.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

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

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityCommentResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.service.CommunityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/community")
@RequiredArgsConstructor
public class CommunityController {
	private final CommunityService communityService;

	// community 생성
	@PostMapping
	public ApiResponse<CommunityResponseDto> SaveCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommnunityCreateRequestDto dto) {

		Long userId = customUserDetails.getId();
		CommunityResponseDto responseDto = communityService.saveCommunity(userId, dto);
		return success(OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

	/**
	 * 게임생활 목록 조회
	 * @param page 0부터 시작
	 * @param size
	 * @return
	 */
	@GetMapping
	public ApiResponse<CommunityListResponseDto> findAllCommunity(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		CommunityListResponseDto responseDtoList = communityService.findAll(page, size);

		return success(OK, COMMUNITY_LIST_FOUND_SUCCESS, responseDtoList);
	}

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

		CommunityResponseDto requestDto = communityService.update(userId, dto);
		return success(OK, COMMUNITY_UPDATE_SUCCESS, requestDto);
	}

	// community 삭제
	@DeleteMapping
	public ApiResponse<Void> deleteCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam Long communityId) {

		Long userId = customUserDetails.getId();

		communityService.delete(userId, communityId);
		return success(OK, COMMUNITY_DELETE_SUCCESS);
	}

	// community 생성(elasticSearch 사용)
	@PostMapping("/es")
	public ApiResponse<CommunityResponseDto> saveCommunityES(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody CommnunityCreateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.saveCommunityES(userId, dto);
		return success(OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

	// community 목록 검색(elasticSearch 사용)
	@GetMapping("/es")
	public ApiResponse<CommunityListResponseDto> findCommunitiesES(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size, @RequestParam String searchKeyword, @RequestParam String game) {

		CommunityListResponseDto responseDtoList = communityService.findCommunitiesES(searchKeyword, game, page, size);
		return success(OK, COMMUNITY_LIST_FOUND_SUCCESS, responseDtoList);
	}

	// community 단건 조회(elasticSearch 사용)
	@GetMapping("/es/{communityId}")
	public ApiResponse<CommunityResponseDto> findCommunityES(@PathVariable String communityId) {
		CommunityResponseDto responseDto = communityService.findCommunityES(communityId);
		return success(OK, COMMUNITY_FOUND_SUCCESS, responseDto);
	}

	// community 수정(elasticSearch 사용)
	@PatchMapping("/es")
	public ApiResponse<CommunityResponseDto> updateCommunityES(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommunityUpdateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.updateCommunityES(userId, dto);
		return success(OK, COMMUNITY_UPDATE_SUCCESS, responseDto);
	}

	// community 삭제(elasticSearch 사용)
	@DeleteMapping("/es")
	public ApiResponse<Void> deleteCommunityES(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam Long communityId) {

		Long userId = customUserDetails.getId();

		communityService.deleteCommunityES(userId, communityId);
		return success(OK, COMMUNITY_DELETE_SUCCESS);
	}

	/**
	 * community 생성(redis활용)
	 * @param customUserDetails 사용자 Id
	 * @param dto title, content, gameId
	 * @return ApiResponse<CommunityResponseDto>
	 */
	@PostMapping("/redis")
	public ApiResponse<CommunityResponseDto> saveCommunityRedis(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommnunityCreateRequestDto dto) {
		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.saveCommunityRedis(userId, dto);
		return success(OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

	/**
	 * community 검색(redis 활용)
	 * @param searchKeyword 검색할 단어
	 * @param page 페이지 수
	 * @param size 한 페이지에 표시할 데이터 수
	 * @return ApiResponse<CommunityListResponseDto>
	 */
	@GetMapping("/redis")
	public ApiResponse<CommunityListResponseDto> findCommunityRedis(@RequestParam String searchKeyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		CommunityListResponseDto responseDto = communityService.findCommunityRedis(searchKeyword, page, size);

		return success(OK, COMMUNITY_FOUND_SUCCESS, responseDto);
	}

	/**
	 * community 수정(redis 활용)
	 * @param customUserDetails 사용자 Id
	 * @param dto communityId, title, content
	 * @return ApiResponse<CommunityResponseDto>
	 */
	@PatchMapping("/redis")
	public ApiResponse<CommunityResponseDto> updateCommunityRedis(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommunityUpdateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto requestDto = communityService.updateCommunityRedis(userId, dto);
		return success(OK, COMMUNITY_UPDATE_SUCCESS, requestDto);
	}

	/**
	 * community 삭제(redis 활용)
	 * @param customUserDetails 사용자 Id
	 * @param communityId community Id
	 * @return ApiResponse<Void>
	 */
	@DeleteMapping("/redis")
	public ApiResponse<Void> deleteCommunityRedis(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam Long communityId) {

		Long userId = customUserDetails.getId();

		communityService.deleteCommunityRedis(userId, communityId);
		return success(OK, COMMUNITY_DELETE_SUCCESS);
	}
}
