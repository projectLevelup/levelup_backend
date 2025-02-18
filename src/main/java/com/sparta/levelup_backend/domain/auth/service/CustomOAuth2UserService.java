package com.sparta.levelup_backend.domain.auth.service;

import com.sparta.levelup_backend.config.CustomOAuth2User;
import com.sparta.levelup_backend.domain.auth.dto.response.GoogleResponse;
import com.sparta.levelup_backend.domain.auth.dto.response.KakaoResponse;
import com.sparta.levelup_backend.domain.auth.dto.response.NaverResponse;
import com.sparta.levelup_backend.domain.auth.dto.response.OAuth2Response;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.UserRole;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if(registrationId.equals("naver")){

            oAuth2Response = new NaverResponse(
                (Map<String, Object>) oAuth2User.getAttributes().get("response"));

        }else if(registrationId.equals("google")){

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        }else {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }


        UserEntity user = null;

        if(userRepository.existsByEmail(oAuth2Response.getEmail())){
            user = userRepository.findByEmailOrElseThrow(oAuth2Response.getEmail());
            if(user.getIsDeleted()){
                throw new OAuth2AuthenticationException(ErrorCode.ALREADY_DELETED_USER.toString());
            }
            if(!user.getProvider().startsWith(registrationId)){
                throw new OAuth2AuthenticationException(ErrorCode.AUTH_TYPE_MISMATCH.toString());
            }


        }else {
            user = UserEntity.builder()
                .email(oAuth2Response.getEmail())
                .nickName(oAuth2Response.getNickName())
                .role(UserRole.USER)
                .provider(registrationId+"new")
                .build();
            userRepository.save(user);
        }




        return new CustomOAuth2User(user);
    }

}
