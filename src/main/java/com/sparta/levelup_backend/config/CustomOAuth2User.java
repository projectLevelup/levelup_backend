package com.sparta.levelup_backend.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

	private final UserEntity user;

	@Override
	public Map<String, Object> getAttributes() {

		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
		grantedAuthority.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {

				return "ROLE_" + user.getRole().toString();
			}
		});

		return grantedAuthority;
	}

	@Override
	public String getName() {

		return user.getEmail();
	}

	public Long getId() {

		return user.getId();
	}

	public String getNickName() {

		return user.getNickName();
	}

	public String getProvider() {
		String google = providerCheck(user.getProvider(), "google");
		String kakao = providerCheck(user.getProvider(), "kakao");
		String naver = providerCheck(user.getProvider(), "naver");
		if (!google.equals("mismatch")) {

			return google;
		} else if (!kakao.equals("mismatch")) {

			return kakao;
		} else if (!naver.equals("mismatch")) {

			return naver;
		} else {

			return "mismatch";
		}
	}

	private String providerCheck(String provider, String providerType) {
		if (provider.startsWith(providerType)) {
			if (provider.startsWith(providerType + "new")) {

				return providerType + "new";
			} else {

				return providerType;
			}
		}

		return "mismatch";
	}

}
