package com.sparta.levelup_backend.domain.user.dto.request;

import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUserRequestDto {

	@JsonProperty(value = "currentPassword")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
		message = PASSWORD_NOT_VALID)
	@NotBlank
	private String currentPassword;
}
