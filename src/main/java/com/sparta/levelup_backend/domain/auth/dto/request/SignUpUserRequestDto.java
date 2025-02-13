package com.sparta.levelup_backend.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sparta.levelup_backend.common.ApiResMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
public class SignUpUserRequestDto {

	@JsonProperty(value = "email")
	@Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = ApiResMessage.EMAIL_NOT_VALID )
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
		message = ApiResMessage.PASSWORD_NOT_VALID)
	@NotBlank
	private String password;

	@JsonProperty(value = "phoneNumber")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = ApiResMessage.PHONE_NUMBER_NOT_VALID)
	@NotBlank
	private String phoneNumber;
}
