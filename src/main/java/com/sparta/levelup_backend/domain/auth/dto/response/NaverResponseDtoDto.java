package com.sparta.levelup_backend.domain.auth.dto.response;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NaverResponseDtoDto implements OAuth2ResponseDto {

	private final Map<String, Object> attribute;

	@Override
	public String getProvider() {

		return "naver";
	}

	@Override
	public String getEmail() {

		return attribute.get("email").toString();
	}

	@Override
	public String getNickName() {

		return attribute.get("name").toString();
	}
}
