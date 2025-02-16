package com.sparta.levelup_backend.proto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sparta.levelup_backend.domain.auth.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Profile("chat-test")
@Configuration
@RequiredArgsConstructor
public class TempSecurityConfig {

	private final TempJwtUtils testJwtUtils;

	private final CustomUserDetailsService customUserDetailsService;

	private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// CSRF 비활성화
			.csrf(csrf -> csrf.disable())

			// 세션을 사용하지 않도록 설정
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			// 요청 권한 설정
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/css/**", "/js/**","/img/**", "/v1/signup", "/signup").permitAll()
				.anyRequest().authenticated())

			// 로그인 경로 설정
			.formLogin(form -> form
				.loginPage("/login")
				.successHandler(jwtAuthenticationSuccessHandler))

			// 로그아웃 설정
			.logout(logout -> logout.permitAll());

		http.addFilterBefore(new TempJwtFilter(testJwtUtils, customUserDetailsService),
							 UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(testPasswordEncoder());
		return authProvider;
	}

	@Bean
	public BCryptPasswordEncoder testPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
