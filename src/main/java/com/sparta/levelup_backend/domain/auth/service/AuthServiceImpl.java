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
import java.util.UUID;

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
            .customerKey(UUID.randomUUID().toString())
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

		String email = userDetails.getUsername();
		Long userId = userDetails.getId();
		String nickName = userDetails.getNickName();
		String role = userDetails.getAuthorities().iterator().next().getAuthority();

		String accessToken = jwtUtils.createAccessToken(email, userId, nickName, role);
		String refreshToken = jwtUtils.createRefreshToken(email, userId, nickName, role);

		ResponseCookie accessCookie = createCookie("accessToken", accessToken, 30 * 60);
		ResponseCookie refreshCookie = createCookie("refreshToken", refreshToken, 12 * 60 * 60);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);
		headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
		headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

		return headers;
	}

	private ResponseCookie createCookie(String name, String token, long maxAge) {
		return ResponseCookie.from(name, jwtUtils.substringToken(token))
			.path("/")
			.domain("localhost")
			.maxAge(maxAge)
			.build();
	}

}