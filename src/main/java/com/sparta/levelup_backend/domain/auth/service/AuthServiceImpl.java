package com.sparta.levelup_backend.domain.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.auth.dto.request.OAuthUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

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
			.provider("none")
            .customerKey(UUID.randomUUID().toString())
			.build();

		userRepository.save(user);
	}

	@Override
	@Transactional
	public void oAuth2signUpUser(OAuthUserRequestDto dto) {
		UserEntity user = userRepository.findByEmailOrElseThrow(dto.getEmail());
		user.updateProvider(user.getProvider().substring(0, user.getProvider().length() - 3));
		user.updatePhoneNumber(dto.getPhoneNumber());
		user.updateEmail(dto.getEmail());
		user.updateNickName(dto.getNickName());
	}

}