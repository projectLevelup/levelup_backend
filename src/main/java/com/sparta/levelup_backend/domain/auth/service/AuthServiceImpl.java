package com.sparta.levelup_backend.domain.auth.service;

import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.auth.dto.request.OAuthUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignInUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.JwtUtils;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;

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

	@Override
	public HttpHeaders authenticate(SignInUserRequestDto dto) {

		CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(dto.getEmail());

		String accessToken = jwtUtils.createAccessToken(
			userDetails.getNickName(),
			userDetails.getId(),
			userDetails.getNickName(),
			userDetails.getAuthorities().iterator().next().getAuthority());

		String refreshToken = jwtUtils.createRefreshToken(
			userDetails.getNickName(),
			userDetails.getId(),
			userDetails.getNickName(),
			userDetails.getAuthorities().iterator().next().getAuthority());

		ResponseCookie accessCookie = ResponseCookie.from("accessToken", jwtUtils.substringToken(accessToken))
			.path("/")
			.domain("localhost")
			.maxAge(30 * 60)
			.build();

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwtUtils.substringToken(refreshToken))
			.path("/")
			.domain("localhost")
			.maxAge(12 * 60 * 60)
			.build();

		log.info("Access token: {} refresh token: {} ", accessToken, refreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", jwtUtils.substringToken(accessToken));
		headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
		headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

		return headers;
	}

}