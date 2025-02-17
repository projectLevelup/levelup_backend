package com.sparta.levelup_backend.domain.community.service;

import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;

public interface CommunityService {
	CommunityResponseDto saveCommunity(Long userId, CommnunityCreateRequestDto dto);

	CommunityListResponseDto findAll(int page, int size);

	CommunityResponseDto update(Long userId, CommunityUpdateRequestDto dto);
}
