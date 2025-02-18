package com.sparta.levelup_backend.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInUserRequestDto {

	@JsonProperty("email")
	private String email;

	@JsonProperty("password")
	private String password;
}
