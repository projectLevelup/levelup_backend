package com.sparta.levelup_backend.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpUserRequestDto {

	@JsonProperty(value = "email")
	@NotBlank
	private String email;

	@JsonProperty(value = "nickName")
	@NotBlank
	private String nickName;

	@JsonProperty(value = "img")
	private String img;

	@JsonProperty(value = "password")
	@NotBlank
	private String password;

	@JsonProperty(value = "phoneNumber")
	@NotBlank
	private String phoneNumber;
}
