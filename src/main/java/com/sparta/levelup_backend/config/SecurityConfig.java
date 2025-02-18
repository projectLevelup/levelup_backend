package com.sparta.levelup_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sparta.levelup_backend.domain.auth.service.CustomUserDetailsService;
import com.sparta.levelup_backend.utill.JwtUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtils jwtUtils;
	private final CustomUserDetailsService userDetailsService;
	private final FilterResponse filterResponse;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return new CustomAccessDeniedHandler(filterResponse);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf((csrf) -> csrf.disable())
			.formLogin((form) -> form.disable())
			.httpBasic((basic) -> basic.disable());

		http.
			authorizeHttpRequests((auth) -> auth
//				.requestMatchers("/","/v1/signin","v1/signup").permitAll()
//				.requestMatchers("/v1/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll());

		http.exceptionHandling(exceptionHandling ->
			exceptionHandling.accessDeniedHandler(accessDeniedHandler()));

		http.
			addFilterBefore(new JwtFilter(jwtUtils, filterResponse), CustomUsernamePasswordAuthenticationFilter.class);

		CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(
			authenticationManager(authenticationConfiguration), jwtUtils, filterResponse);
		customUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/v1/signin");

		http.
			addFilterAt(customUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();



	}
}
