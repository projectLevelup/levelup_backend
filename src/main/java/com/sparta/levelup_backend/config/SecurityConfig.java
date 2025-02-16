package com.sparta.levelup_backend.config;

import com.sparta.levelup_backend.domain.auth.service.CustomOAuth2UserService;
import com.sparta.levelup_backend.domain.auth.service.CustomUserDetailsService;
import com.sparta.levelup_backend.utill.JwtUtils;
import lombok.RequiredArgsConstructor;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtUtils jwtUtils;
	private final CustomOAuth2UserService customOAuth2UserService;
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
			.formLogin((form) -> form
				.loginPage("/v2/signin"))
			.httpBasic((basic) -> basic.disable());

		/**
		 * 이 부분에서 customOAuth2UserService를 연결해줍니다.
		 * 데이터를 받으면 이쪽 서비스로 자동적으로 넣어줍니다.
		 */
		http.
					oauth2Login((oauth2)
						-> oauth2.userInfoEndpoint((userInfoEndpointConfig)
						-> userInfoEndpointConfig.userService(customOAuth2UserService)));


		http.oauth2Login((auth)
				-> auth.authorizationEndpoint((authorizationEndPointConfig)
				-> authorizationEndPointConfig.baseUri("/v2/signin/oauth2/authorization") )
			);


		http.
			authorizeHttpRequests((auth) -> auth
				.requestMatchers("/","/v2/sign**").permitAll()
				.requestMatchers("/v2/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated());

		http.exceptionHandling(exceptionHandling ->
			exceptionHandling.accessDeniedHandler(accessDeniedHandler()));

		http.
			addFilterBefore(new JwtFilter(jwtUtils, filterResponse), CustomUsernamePasswordAuthenticationFilter.class);

		CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(
			authenticationManager(authenticationConfiguration), jwtUtils, filterResponse);
		customUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/v2/signinSend");

		http.
			addFilterAt(customUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement((session) -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();



	}
}
