package com.sparta.levelup_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.levelup_backend.domain.auth.dto.request.SignInUserRequestDto;
import com.sparta.levelup_backend.utill.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws
        AuthenticationException {

        String email = " ";
        String password = " ";

        if (request.getContentType() != null && request.getContentType()
            .equals(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                SignInUserRequestDto signInUserRequestDto = objectMapper.readValue(
                    request.getReader().lines().collect(Collectors.joining()),
                    SignInUserRequestDto.class);

                email = signInUserRequestDto.getEmail();
                password = signInUserRequestDto.getPassword();

            } catch (IOException e) {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
            }
        } else {
            email = " ";
            password = " ";
        }
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

        Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorites.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtils.createToken(username, role);

        response.addHeader("Authorization", token);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(token);
        response.getWriter().flush();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws
        IOException,
        ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("로그인에 실패하였습니다.");
        response.getWriter().flush();
    }
}
