package com.sparta.levelup_backend.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;

public class CustomUserDetails implements UserDetails {

	UserEntity user;

	public CustomUserDetails(UserEntity user) {
		this.user = user;
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

	public UserEntity getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	public Long getId() {
		return user.getId();
	}
  
	public String getNickName() {
		return user.getNickName();
	}
}
