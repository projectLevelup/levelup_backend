package com.sparta.levelup_backend.domain.user.controller;

import com.sparta.levelup_backend.common.ApiResMessage;
import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.service.UserService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users/{user_id}")
    public ApiResponse<UserResponseDto> findUserById(
        @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long user_id) {
        String role = customUserDetails.
            getAuthorities().
            iterator().next().
            getAuthority();
        UserResponseDto responseDto = userService.findUserById(role, user_id);
        return ApiResponse.success(HttpStatus.OK, ApiResMessage.FIND_SUCCESS, responseDto);
    }


}
