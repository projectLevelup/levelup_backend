package com.sparta.levelup_backend.domain.auth.service;

import static com.sparta.levelup_backend.domain.user.dto.UserMessage.CONGRATULATION_SIGNUP;
import static com.sparta.levelup_backend.enums.ErrorCode.PASSWORD_INCORRECT;
import static com.sparta.levelup_backend.enums.ProviderType.NONE;
import static com.sparta.levelup_backend.enums.UserRole.USER;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.auth.dto.request.OAuthUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignInUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;
import com.sparta.levelup_backend.domain.email.event.EmailEventPublisher;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.enums.ProviderType;
import com.sparta.levelup_backend.exception.user.UserException;
import com.sparta.levelup_backend.utill.JwtUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void signUpUser(SignUpUserRequestDto dto) {

        userRepository.existsByEmailOrElseThrow(dto.getEmail());

        UserEntity user = UserEntity.builder().
            email(dto.getEmail())
            .nickName(dto.getNickName())
            .password(bCryptPasswordEncoder.encode(dto.getPassword()))
            .imgUrl(dto.getImgUrl())
            .role(USER)
            .phoneNumber(dto.getPhoneNumber())
            .customerKey(UUID.randomUUID().toString())
            .provider(NONE)
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
            user.getProvider().toString()
                .substring(0, user.getProvider().toString().length() - 3)));
        user.updatePhoneNumber(dto.getPhoneNumber());
        user.updateEmail(dto.getEmail());
        user.updateNickName(dto.getNickName());
    }

    @Override
    public HttpHeaders authenticate(SignInUserRequestDto dto) {

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(
            dto.getEmail());

        if (!bCryptPasswordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new UserException(PASSWORD_INCORRECT);
        }

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
        headers.add(SET_COOKIE, accessCookie.toString());
        headers.add(SET_COOKIE, refreshCookie.toString());

        return headers;
    }

    private ResponseCookie createCookie(String name, String token, long maxAge) {
        return ResponseCookie.from(name, jwtUtils.substringToken(token))
            .path("/")
            .maxAge(maxAge)
            .build();
    }

}