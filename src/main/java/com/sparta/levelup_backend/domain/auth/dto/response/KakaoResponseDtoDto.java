package com.sparta.levelup_backend.domain.auth.dto.response;

import java.util.Map;

public class KakaoResponseDtoDto implements OAuth2ResponseDto {

	private final Map<String, Object> attribute;
	private final Map<String, Object> account;
	private final Map<String, Object> profile;

	public KakaoResponseDtoDto(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.account = (Map<String, Object>)attribute.get("kakao_account");
		this.profile = (Map<String, Object>)attribute.get("profile");
	}

	@Override
	public String getProvider() {

		return "kakao";
	}

	@Override
	public String getEmail() {

		return account.get("email").toString();
	}

	@Override
	public String getNickName() {

		return profile.get("nickname").toString();
	}
}
