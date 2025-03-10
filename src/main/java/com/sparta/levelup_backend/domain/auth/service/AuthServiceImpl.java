package com.sparta.levelup_backend.domain.auth.service;

import static com.sparta.levelup_backend.domain.user.dto.UserMessage.*;

import com.sparta.levelup_backend.enums.ProviderType;
import com.sparta.levelup_backend.exception.user.PasswordIncorrectException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.auth.dto.request.OAuthUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignInUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;
import com.sparta.levelup_backend.domain.email.event.EmailEventPublisher;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.JwtUtils;
import com.sparta.levelup_backend.enums.UserRole;

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
	private final EmailEventPublisher emailEventPublisher;

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
			.provider(ProviderType.NONE)
            .customerKey(UUID.randomUUID().toString())
			.build();

		userRepository.save(user);
		emailEventPublisher.publisher(
			new SendEmailDto(user.getEmail(), CONGRATULATION_SIGNUP, CONGRATULATION_SIGNUP));

	}

	@Override
	@Transactional
	public void oAuth2signUpUser(OAuthUserRequestDto dto) {
		UserEntity user = userRepository.findByEmailOrElseThrow(dto.getEmail());
		user.updateProvider(ProviderType.valueOf(
            user.getProvider().toString().substring(0, user.getProvider().toString().length() - 3)));
		user.updatePhoneNumber(dto.getPhoneNumber());
		user.updateEmail(dto.getEmail());
		user.updateNickName(dto.getNickName());
	}

	@Override
	public HttpHeaders authenticate(SignInUserRequestDto dto) {

		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(dto.getEmail());

		if(bCryptPasswordEncoder.matches(dto.getPassword(),userDetails.getPassword())) {
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
		}else {
			throw new PasswordIncorrectException();
		}
	}

	private ResponseCookie createCookie(String name, String token, long maxAge) {
		return ResponseCookie.from(name, jwtUtils.substringToken(token))
			.path("/")
			.maxAge(maxAge)
			.build();
	}

}