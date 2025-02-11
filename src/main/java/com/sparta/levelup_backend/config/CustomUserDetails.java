package com.sparta.levelup_backend.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;

public class CustomUserDetails implements UserDetails {

	UserEntity user;

	public CustomUserDetails(UserEntity user){
	this.user = user;
	}

	/**
	 * 권한정보를 JWT토큰에 넘기기 위해서 반드시 getAuthorities를 써야만 하는가?
	 * 다른 방법으로 넘길수는 없을까?
	 *
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
		grantedAuthority.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole().toString();
			}
		});

		return grantedAuthority;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
}
