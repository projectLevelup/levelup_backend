package com.sparta.levelup_backend.domain.auth.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.config.CustomOAuth2User;
import com.sparta.levelup_backend.domain.auth.dto.response.GoogleResponseDtoDto;
import com.sparta.levelup_backend.domain.auth.dto.response.KakaoResponseDtoDto;
import com.sparta.levelup_backend.domain.auth.dto.response.NaverResponseDtoDto;
import com.sparta.levelup_backend.domain.auth.dto.response.OAuth2ResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		OAuth2ResponseDto oAuth2ResponseDto = null;
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		if (registrationId.equals("naver")) {

			oAuth2ResponseDto = new NaverResponseDtoDto(
				(Map<String, Object>)oAuth2User.getAttributes().get("response"));

		} else if (registrationId.equals("google")) {

			oAuth2ResponseDto = new GoogleResponseDtoDto(oAuth2User.getAttributes());

		} else {
			oAuth2ResponseDto = new KakaoResponseDtoDto(oAuth2User.getAttributes());
		}

		UserEntity user = null;

		if (userRepository.existsByEmail(oAuth2ResponseDto.getEmail())) {
			user = userRepository.findByEmailOrElseThrow(oAuth2ResponseDto.getEmail());
			if (user.getIsDeleted()) {
				throw new OAuth2AuthenticationException(ErrorCode.ALREADY_DELETED_USER.toString());
			}
			if (!user.getProvider().startsWith(registrationId)) {
				throw new OAuth2AuthenticationException(ErrorCode.AUTH_TYPE_MISMATCH.toString());
			}
		} else {
			user = UserEntity.builder()
				.email(oAuth2ResponseDto.getEmail())
				.nickName(oAuth2ResponseDto.getNickName())
				.role(UserRole.USER)
				.provider(registrationId + "new")
				.build();
			userRepository.save(user);
		}

		return new CustomOAuth2User(user);
	}

}
