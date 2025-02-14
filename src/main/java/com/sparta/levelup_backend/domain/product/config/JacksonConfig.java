package com.sparta.levelup_backend.domain.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
			.registerModule(new JavaTimeModule())  // ⬅️ LocalDateTime 직렬화 지원
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ⬅️ timestamp 대신 ISO-8601 형식 사용
	}
}
