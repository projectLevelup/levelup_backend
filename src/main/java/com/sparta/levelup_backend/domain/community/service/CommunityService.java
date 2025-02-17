package com.sparta.levelup_backend.domain.community.service;

import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;

public interface CommunityService {
	CommunityResponseDto saveCommunity(Long userId, CommnunityCreateRequestDto dto);
}
