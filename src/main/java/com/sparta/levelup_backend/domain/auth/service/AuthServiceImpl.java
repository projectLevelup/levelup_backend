package com.sparta.levelup_backend.domain.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public void signUpUser(SignUpUserRequestDto signUpUserRequestDto) {

			userRepository.existsByEmailOrElseThrow(signUpUserRequestDto.getEmail());

			UserEntity user = UserEntity.builder().
				email(signUpUserRequestDto.getEmail())
				.nickName(signUpUserRequestDto.getNickName())
				.password(bCryptPasswordEncoder.encode(signUpUserRequestDto.getPassword()))
				.imgUrl(signUpUserRequestDto.getImgUrl())
				.role(UserRole.USER)
				.phoneNumber(signUpUserRequestDto.getPhoneNumber())
				.build();

			userRepository.save(user);
		}

	}