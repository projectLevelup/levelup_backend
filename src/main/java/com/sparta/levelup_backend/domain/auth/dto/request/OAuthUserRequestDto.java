package com.sparta.levelup_backend.domain.auth.dto.request;

import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.levelup_backend.config.annotaion.FormToJson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@FormToJson
public class OAuthUserRequestDto {

	@JsonProperty(value = "email")
	@Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = EMAIL_NOT_VALID)
	@NotBlank
	private String email;

	@JsonProperty(value = "nickName")
	@NotBlank
	private String nickName;

	@JsonProperty(value = "phoneNumber")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = PHONE_NUMBER_NOT_VALID)
	@NotBlank
	private String phoneNumber;

}
