package com.sparta.levelup_backend.domain.community.service;

import org.springframework.data.domain.Pageable;

import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityCommentResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;

public interface CommunityService {
	CommunityResponseDto saveCommunity(Long userId, CommnunityCreateRequestDto dto);

	CommunityListResponseDto findAllByGameName(String gameName, Pageable pageable);

	CommunityCommentResponseDto findById(Long communityId);

	CommunityListResponseDto findCommunities(String searchKeyword, String gameName, int page, int size);

	CommunityResponseDto updateCommunity(Long userId, CommunityUpdateRequestDto dto);

	void deleteCommunity(Long userId, Long communityId);
}
