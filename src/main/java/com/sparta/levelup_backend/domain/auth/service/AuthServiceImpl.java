package com.sparta.levelup_backend.domain.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void signUpUser(SignUpUserRequestDto signUpUserRequestDto) {
		// 회원가입시 이메일 중복여부 확인.
		if (userRepository.existsByEmail(signUpUserRequestDto.getEmail())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "임시 오류: 사용자가 중복됩니다.");
		} else {

			UserEntity user = UserEntity.builder().
				email(signUpUserRequestDto.getEmail())
				.nickName(signUpUserRequestDto.getNickName())
				.password(bCryptPasswordEncoder.encode(signUpUserRequestDto.getPassword()))
				.img(signUpUserRequestDto.getImg())
				.role(UserRole.USER)
				.phoneNumber(signUpUserRequestDto.getPhoneNumber())
				.build();

			userRepository.save(user);
		}

	}
}