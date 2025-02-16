package com.sparta.levelup_backend.config;

import static com.sparta.levelup_backend.common.ApiResMessage.*;

import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final FilterResponse filterResponse;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws
        AuthenticationException {
        request.getCookies();

        setUsernameParameter("email");

        String email = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,
            password);

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain,
        Authentication authentication) throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Long id = customUserDetails.getId();
        String nickName = customUserDetails.getNickName();



        Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorites.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtils.createAccessToken(username, id, nickName, role);
        String refreshToken = jwtUtils.createRefreshToken(username, id, nickName, role);

        response.addHeader("Authorization", accessToken);

        /**
         * 일단 쿠키에 토큰 저장.
         */

//        Cookie cookie = new Cookie("Authorization",token);
//        cookie.setDomain("kubernetes.docker.internal");
//        response.addCookie(cookie);
        response.addHeader("Set-Cookie","accessToken="+accessToken);
        response.addHeader("Set-Cookie","refreshToken="+refreshToken);
        response.addHeader("Domain","localhost" );
        filterResponse.responseSuccessMsg(response, HttpStatus.OK, LOGIN_SUCCESS, accessToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws
        IOException,
        ServletException {

        if (response.getStatus() == 200 && failed.getCause() != null) {
            ErrorCode errorCode = ErrorCode.from(failed.getCause().getMessage());

            filterResponse.responseErrorMsg(response,
                errorCode.getStatus().value(),
                errorCode.getCode(),
                failed.getCause().getMessage());
        } else {
            filterResponse.responseErrorMsg(response,
                ErrorCode.LOGIN_FAILED.getStatus().value(),
                ErrorCode.LOGIN_FAILED.getCode(),
                ErrorCode.LOGIN_FAILED.getMessage());
        }
    }


}
