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

	CommunityListResponseDto findCommunities(String gameName, String searchKeyWord, Pageable pageable);

	CommunityCommentResponseDto findById(Long communityId);

	CommunityResponseDto update(Long userId, CommunityUpdateRequestDto dto);

	void delete(Long userId, Long communityId);

	CommunityListResponseDto findCommunitiesES(String searchKeyword, String gameName, int page, int size);

	CommunityResponseDto saveCommunityES(Long userId, CommnunityCreateRequestDto dto);

	CommunityResponseDto updateCommunityES(Long userId, CommunityUpdateRequestDto dto);

	void deleteCommunityES(Long userId, Long communityId);

	CommunityResponseDto findCommunityES(String communityId);
}
