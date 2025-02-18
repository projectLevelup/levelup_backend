package com.sparta.levelup_backend.domain.auth.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.AUTH_TYPE_MISMATCH;

import com.sparta.levelup_backend.exception.common.AlreadyDeletedUserException;
import com.sparta.levelup_backend.exception.common.MismatchException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmailOrElseThrow(email);
		if(user.getIsDeleted()){
			throw new AlreadyDeletedUserException();
		}
		String provider = user.getProvider();
		if(!provider.startsWith("none")) {
			throw new MismatchException(AUTH_TYPE_MISMATCH);
		}

		return new CustomUserDetails(user);
	}
}
