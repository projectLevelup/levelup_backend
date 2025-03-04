package com.sparta.levelup_backend.domain.community.service;

import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityCommentResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;

public interface CommunityService {
	CommunityResponseDto saveCommunity(Long userId, CommnunityCreateRequestDto dto);

	CommunityListResponseDto findAll(int page, int size);

	CommunityCommentResponseDto findById(Long communityId);

	CommunityResponseDto update(Long userId, CommunityUpdateRequestDto dto);

	void delete(Long userId, Long communityId);

	CommunityListResponseDto findCommunitiesES(String searchKeyword, int page, int size);

	CommunityResponseDto saveCommunityES(Long userId, CommnunityCreateRequestDto dto);

	CommunityResponseDto updateCommunityES(Long userId, CommunityUpdateRequestDto dto);

	void deleteCommunityES(Long userId, Long communityId);

	CommunityResponseDto findCommunityES(String communityId);

	CommunityResponseDto saveCommunityRedis(Long userId, CommnunityCreateRequestDto dto);

	CommunityListResponseDto findCommunityRedis(String searchKeyword, int page, int size);

	CommunityResponseDto updateCommunityRedis(Long userId, CommunityUpdateRequestDto dto);

	void deleteCommunityRedis(Long userId, Long communityId);
}
