package com.sparta.levelup_backend.domain.user.controller;

import static com.sparta.levelup_backend.common.ApiResponse.success;

import com.sparta.levelup_backend.common.ApiResMessage;
import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.dto.request.ChangePasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserImgUrlReqeustDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users/{userId}")
    public ApiResponse<UserResponseDto> findUserById(
        @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long userId) {
        String role = customUserDetails.
            getAuthorities().
            iterator().next().
            getAuthority();
        UserResponseDto responseDto = userService.findUserById(role, userId);
        return success(HttpStatus.OK, ApiResMessage.FIND_SUCCESS, responseDto);
    }

    @GetMapping("/users")
    public ApiResponse<UserResponseDto> findUser(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        UserResponseDto responseDto = userService.findUser(customUserDetails.getId());
        return success(HttpStatus.OK, ApiResMessage.FIND_SUCCESS, responseDto);
    }

    @PatchMapping("/users")
    public ApiResponse<UserResponseDto> updateUser(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody UpdateUserRequestDto dto
    ) {

        UserResponseDto responseDto = userService.updateUser(customUserDetails.getId(), dto);
        return success(HttpStatus.OK, ApiResMessage.UPDATE_SUCCESS, responseDto);
    }

    @PatchMapping("/users/changingPassword")
    public ApiResponse<Void> changePassword(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(customUserDetails.getId(), dto);
        return success(HttpStatus.OK, ApiResMessage.PASSWORD_CHANGE_SUCCESS);
    }

    @PatchMapping("/users/profileImage")
    public ApiResponse<UserResponseDto> updateImgUrl(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody UpdateUserImgUrlReqeustDto dto
    ){
        UserResponseDto responseDto = userService.updateImgUrl(customUserDetails.getId(),dto);
        return success(HttpStatus.OK, ApiResMessage.UPDATE_SUCCESS, responseDto);
    }

    @DeleteMapping("/users")
    public ApiResponse<Void> deleteUser(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody DeleteUserRequestDto dto
    ){
        userService.deleteUser(customUserDetails.getId(),dto);
        return ApiResponse.success(HttpStatus.OK, ApiResMessage.DELETE_SUCCESS);
    }

}
