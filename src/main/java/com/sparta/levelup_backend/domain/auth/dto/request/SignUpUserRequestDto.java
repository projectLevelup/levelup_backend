package com.sparta.levelup_backend.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
public class SignUpUserRequestDto {

	@JsonProperty(value = "email")
	@NotBlank
	private String email;

	@JsonProperty(value = "nickName")
	@NotBlank
	private String nickName;

	@JsonProperty(value = "imgUrl")
	@URL
	private String imgUrl;

	@JsonProperty(value = "password")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
		message = "비밀번호는 최소 8자 이상이며, 대소문자, 숫자, 특수문자를 각각 최소 1자 이상 포함해야 합니다.")
	@NotBlank
	private String password;

	@JsonProperty(value = "phoneNumber")
	@NotBlank
	private String phoneNumber;
}
