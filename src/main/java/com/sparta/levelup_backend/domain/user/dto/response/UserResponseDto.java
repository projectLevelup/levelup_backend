package com.sparta.levelup_backend.domain.user.dto.response;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponseDto {

    private String email;

    private String nickName;

    private String imgUrl;

    private String phoneNumber;

    public static UserResponseDto of(UserEntity user){
        return new UserResponseDto(
            user.getEmail(),
            user.getNickName(),
            user.getImgUrl(),
            user.getPhoneNumber()
        );
    }
}
