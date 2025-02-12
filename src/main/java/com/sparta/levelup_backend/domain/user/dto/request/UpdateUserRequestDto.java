package com.sparta.levelup_backend.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "nickName")
    private String nickName;

    @JsonProperty(value = "imgUrl")
    private String imgUrl;

    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
}
