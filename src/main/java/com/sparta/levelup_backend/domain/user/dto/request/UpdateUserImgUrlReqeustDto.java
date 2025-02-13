package com.sparta.levelup_backend.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
@AllArgsConstructor
public class UpdateUserImgUrlReqeustDto {

    @URL
    private final String imgUrl;

}
