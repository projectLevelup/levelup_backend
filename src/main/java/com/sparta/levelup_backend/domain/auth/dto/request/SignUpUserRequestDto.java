package com.sparta.levelup_backend.domain.auth.dto.request;

import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.EMAIL_NOT_VALID;
import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.PASSWORD_NOT_VALID;
import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.PHONE_NUMBER_NOT_VALID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.levelup_backend.config.annotaion.FormToJson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
@FormToJson
public class SignUpUserRequestDto {

	@JsonProperty(value = "email")
	@Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = EMAIL_NOT_VALID )
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
		message = PASSWORD_NOT_VALID)
	@NotBlank
	private String password;

	@JsonProperty(value = "phoneNumber")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = PHONE_NUMBER_NOT_VALID)
	@NotBlank
	private String phoneNumber;
}
