package com.sparta.levelup_backend.domain.auth.service;

import com.sparta.levelup_backend.exception.common.AlreadyDeletedUserException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.MismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmailOrElseThrow(email);
		if(user.getIsDeleted()){
			throw new AlreadyDeletedUserException();
		}
		String password = user.getPassword();
		if(bCryptPasswordEncoder.matches("google",password) ||
		bCryptPasswordEncoder.matches("kakao",password) ||
		bCryptPasswordEncoder.matches("naver",password) ) {
			throw new MismatchException(ErrorCode.AUTH_TYPE_MISMATCH);
		}

		return new CustomUserDetails(user);
	}
}
