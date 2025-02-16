package com.sparta.levelup_backend.domain.auth.service;

import com.sparta.levelup_backend.config.CustomOAuth2User;
import com.sparta.levelup_backend.domain.auth.dto.response.GoogleResponse;
import com.sparta.levelup_backend.domain.auth.dto.response.NaverResponse;
import com.sparta.levelup_backend.domain.auth.dto.response.OAuth2Response;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.AlreadyDeletedUserException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.MismatchException;
import com.sparta.levelup_backend.utill.UserRole;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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

        }


        UserEntity user = null;

        if(userRepository.existsByEmail(oAuth2Response.getEmail())){
            user = userRepository.findByEmailOrElseThrow(oAuth2Response.getEmail());
            if(user.getIsDeleted()){
                throw new AlreadyDeletedUserException();
            }
            if(bCryptPasswordEncoder.matches(registrationId,user.getPassword())){
                throw new MismatchException(ErrorCode.AUTH_TYPE_MISMATCH);
            }


        }else {
            user = UserEntity.builder()
                .email(oAuth2Response.getEmail())
                .nickName(oAuth2Response.getNickName())
                .role(UserRole.USER)
                .password(bCryptPasswordEncoder.encode(registrationId))
                .build();
            userRepository.save(user);
        }




        return new CustomOAuth2User(user);
    }

}
