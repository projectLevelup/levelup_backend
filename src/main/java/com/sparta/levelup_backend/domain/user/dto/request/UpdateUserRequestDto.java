package com.sparta.levelup_backend.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "nickName")
    private String nickName;

    @JsonProperty(value = "imgUrl")
    @URL
    private String imgUrl;

    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
}
