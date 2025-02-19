package com.sparta.levelup_backend.domain.user.dto.request;

import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.EMAIL_NOT_VALID;
import static com.sparta.levelup_backend.domain.user.dto.UserValidMessage.PHONE_NUMBER_NOT_VALID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

    @JsonProperty(value = "email")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = EMAIL_NOT_VALID)
    private String email;

    @JsonProperty(value = "nickName")
    private String nickName;

    @JsonProperty(value = "imgUrl")
    @URL
    private String imgUrl;

    @JsonProperty(value = "phoneNumber")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = PHONE_NUMBER_NOT_VALID)
    private String phoneNumber;
}
