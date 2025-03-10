package com.sparta.levelup_backend.domain.auth.service;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import com.sparta.levelup_backend.enums.ProviderType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.user.AlreadyDeletedUserException;
import com.sparta.levelup_backend.exception.common.MismatchException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmailOrElseThrow(email);
		if (user.getIsDeleted()) {
			throw new AlreadyDeletedUserException();
		}
		if (!user.getProvider().equals(ProviderType.NONE)) {
			throw new MismatchException(AUTH_TYPE_MISMATCH);
		}

		return new CustomUserDetails(user);
	}
}
