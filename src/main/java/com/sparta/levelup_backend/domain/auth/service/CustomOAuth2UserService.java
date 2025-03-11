package com.sparta.levelup_backend.domain.auth.service;

import static com.sparta.levelup_backend.domain.user.dto.UserMessage.CONGRATULATION_SIGNUP;
import static com.sparta.levelup_backend.enums.ErrorCode.ALREADY_DELETED_USER;
import static com.sparta.levelup_backend.enums.ErrorCode.AUTH_TYPE_MISMATCH;
import static com.sparta.levelup_backend.enums.ProviderType.GOOGLE;
import static com.sparta.levelup_backend.enums.ProviderType.NAVER;
import static com.sparta.levelup_backend.enums.UserRole.USER;

import com.sparta.levelup_backend.common.security.CustomOAuth2User;
import com.sparta.levelup_backend.domain.auth.dto.response.GoogleResponseDto;
import com.sparta.levelup_backend.domain.auth.dto.response.NaverResponseDto;
import com.sparta.levelup_backend.domain.auth.dto.response.OAuth2ResponseDto;
import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;
import com.sparta.levelup_backend.domain.email.event.EmailEventPublisher;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.enums.ProviderType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final EmailEventPublisher emailEventPublisher;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2ResponseDto oAuth2ResponseDto = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId.toUpperCase().equals(NAVER.toString())) {
            oAuth2ResponseDto = new NaverResponseDto(
                (Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        if (registrationId.toUpperCase().equals(GOOGLE.toString())) {
            oAuth2ResponseDto = new GoogleResponseDto(oAuth2User.getAttributes());
        }

        try {

            UserEntity user = userRepository.findByEmailOrElseThrow(oAuth2ResponseDto.getEmail());

            if (user.getIsDeleted()) {
                throw new OAuth2AuthenticationException(ALREADY_DELETED_USER.toString());
            }

            if (!user.getProvider().toString().startsWith(registrationId.toUpperCase())) {
                throw new OAuth2AuthenticationException(AUTH_TYPE_MISMATCH.toString());
            }

            return new CustomOAuth2User(user);

        } catch (Exception e) {

            UserEntity user = UserEntity.builder()
                .email(oAuth2ResponseDto.getEmail())
                .nickName(oAuth2ResponseDto.getNickName())
                .role(USER)
                .provider(ProviderType.valueOf((registrationId + "new").toUpperCase()))
                .build();

            userRepository.save(user);

            emailEventPublisher.publisher(
                new SendEmailDto(user.getEmail(), CONGRATULATION_SIGNUP, CONGRATULATION_SIGNUP));

            return new CustomOAuth2User(user);

        }

    }
}
