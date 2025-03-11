package com.sparta.levelup_backend.domain.user.controller;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.DELETE_SUCCESS;
import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.FIND_SUCCESS;
import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.PASSWORD_CHANGE_SUCCESS;
import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.RESET_EMAIL_SEND_SUCCESS;
import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.RESET_PASSWORD_SUCCESS;
import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.UPDATE_SUCCESS;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.success;
import static org.springframework.http.HttpStatus.OK;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.dto.request.ChangePasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.request.ResetPasswordConfirmDto;
import com.sparta.levelup_backend.domain.user.dto.request.ResetPasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserImgUrlReqeustDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/admin/users/{userId}")
	public ApiResponse<UserResponseDto> findUserById(
			@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long userId) {

		String role = customUserDetails.
				getAuthorities().
				iterator().next().
				getAuthority();

		return success(OK, FIND_SUCCESS, userService.findUserById(role, userId));
	}

	@GetMapping("/users")
	public ApiResponse<UserResponseDto> findUser(
			@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		return success(OK, FIND_SUCCESS, userService.findUser(customUserDetails.getId()));
	}

	@PatchMapping("/users")
	public ApiResponse<UserResponseDto> updateUser(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody UpdateUserRequestDto dto
	) {



		return success(OK, UPDATE_SUCCESS, userService.updateUser(customUserDetails.getId(), dto));
	}

	@PatchMapping("/users/changingPassword")
	public ApiResponse<Void> changePassword(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody ChangePasswordDto dto) {
		userService.changePassword(customUserDetails.getId(), dto);

		return success(OK, PASSWORD_CHANGE_SUCCESS);
	}

	@PatchMapping("/users/profileImage")
	public ApiResponse<UserResponseDto> updateImgUrl(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody UpdateUserImgUrlReqeustDto dto
	) {
		return success(OK, UPDATE_SUCCESS, userService.updateImgUrl(customUserDetails.getId(), dto));
	}

	@DeleteMapping("/users")
	public ApiResponse<Void> deleteUser(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody DeleteUserRequestDto dto
	) {
		userService.deleteUser(customUserDetails.getId(), dto);

		return success(OK, DELETE_SUCCESS);
	}

	@PostMapping("/users/resetPassword")
	public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
		userService.resetPassword(dto);
		return success(OK, RESET_EMAIL_SEND_SUCCESS);
	}

	@PostMapping("/users/resetPasswordConfirm")
	public ApiResponse<Void> resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDto dto) {
		userService.resetPasswordConfirm(dto);
		return success(OK, RESET_PASSWORD_SUCCESS);
	}
}
