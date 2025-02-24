package com.sparta.levelup_backend.domain.community.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import com.sparta.levelup_backend.domain.community.document.CommunityDocument;
import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.repositoryES.CommunityESRepository;
import com.sparta.levelup_backend.domain.community.service.CommunityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/community")
@RequiredArgsConstructor
public class CommunityController {
	private final CommunityService communityService;

	private final CommunityESRepository communityESRepository;

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

	@PatchMapping
	public ApiResponse<CommunityResponseDto> updateCommunity(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommunityUpdateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto requestDto = communityService.update(userId, dto);
		return success(OK, COMMUNITY_UPDATE_SUCCESS, requestDto);
	}

	@DeleteMapping
	public ApiResponse<Void> deleteCommunity(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam Long communityId) {

		Long userId = customUserDetails.getId();

		communityService.delete(userId, communityId);
		return success(OK, COMMUNITY_DELETE_SUCCESS);
	}

	@PostMapping("/es")
	public ApiResponse<CommunityResponseDto> saveCommunityES(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody CommnunityCreateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.saveCommunityES(userId, dto);
		return success(OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

	@GetMapping("/es")
	public ApiResponse<CommunityListResponseDto> findCommunitiesES(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size, @RequestParam String searchKeyword) {

		CommunityListResponseDto responseDtoList = communityService.findCommunitiesES(searchKeyword, page, size);
		return success(OK, COMMUNITY_LIST_FOUND_SUCCESS, responseDtoList);
	}

	@GetMapping("/es/{communityId}")
	public ApiResponse<CommunityResponseDto> findCommunityES(@PathVariable String communityId) {
		CommunityResponseDto responseDto = communityService.findCommunityES(communityId);
		return success(OK, COMMUNITY_FOUND_SUCCESS, responseDto);
	}

	@PatchMapping("/es")
	public ApiResponse<CommunityResponseDto> updateCommunityES(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommunityUpdateRequestDto dto) {

		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.updateCommunityES(userId, dto);
		return success(OK, COMMUNITY_UPDATE_SUCCESS, responseDto);
	}

	@DeleteMapping("/es")
	public ApiResponse<Void> deleteCommunityES(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam Long communityId) {

		Long userId = customUserDetails.getId();

		communityService.deleteCommunityES(userId, communityId);
		return success(OK, COMMUNITY_DELETE_SUCCESS);
	}

	@PostMapping("/redis")
	public ApiResponse<CommunityResponseDto> saveCommunityRedis(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommnunityCreateRequestDto dto) {
		Long userId = customUserDetails.getId();

		CommunityResponseDto responseDto = communityService.saveCommunityRedis(userId, dto);
		return success(OK, COMMUNITY_SAVE_SUCCESS, responseDto);
	}

	@GetMapping("/redis")
	public ApiResponse<CommunityListResponseDto> findCommunityRedis(@RequestParam String searchKeyword) {

		CommunityListResponseDto responseDto = communityService.findCommunityRedis(searchKeyword);

		return success(OK, COMMUNITY_FOUND_SUCCESS, responseDto);
	}

	@GetMapping("/es/test")
	public ApiResponse<List<CommunityDocument>> findAllComunity() {
		List<CommunityDocument> communityDocuments = StreamSupport
			.stream(communityESRepository.findAll().spliterator(), false)
			.collect(Collectors.toList());
		return success(OK, COMMUNITY_LIST_FOUND_SUCCESS, communityDocuments);
	}
}
