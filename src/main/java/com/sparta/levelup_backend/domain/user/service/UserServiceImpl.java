package com.sparta.levelup_backend.domain.user.service;

import static com.sparta.levelup_backend.domain.sse.dto.request.AlertMessageDto.*;
import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.sse.entity.AlertMessageEntity;
import com.sparta.levelup_backend.domain.sse.entity.AlertMessageLogEntity;
import com.sparta.levelup_backend.domain.sse.event.AlertEventPublisher;
import com.sparta.levelup_backend.domain.sse.repository.AlertMessageLogRepository;
import com.sparta.levelup_backend.domain.sse.repository.AlertMessageRepository;
import com.sparta.levelup_backend.domain.user.dto.request.ChangePasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserImgUrlReqeustDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.CurrentPasswordNotMatchedException;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.PasswordConfirmNotMatchedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AlertMessageRepository alertMessageRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final AlertEventPublisher alertEvent;
	private final AlertMessageLogRepository alertMessageLogRepository;

	@Override
	public UserResponseDto findUserById(String role, Long id) {

		if (role.equals("ROLE_ADMIN")) {
			UserEntity user = userRepository.findByIdOrElseThrow(id);

			return UserResponseDto.from(user);
		}
		throw new ForbiddenException(FORBIDDEN_ACCESS);
	}

	@Override
	public UserResponseDto findUser(Long id) {
		UserEntity user = userRepository.findByIdOrElseThrow(id);

		return UserResponseDto.from(user);
	}

	@Override
	@Transactional
	public UserResponseDto updateUser(Long id, UpdateUserRequestDto dto) {

		UserEntity user = userRepository.findByIdOrElseThrow(id);

		if (dto.getEmail() != null) {
			userRepository.existsByEmailOrElseThrow(dto.getEmail());
			user.updateEmail(dto.getEmail());
		}

		if (dto.getNickName() != null) {
			user.updateNickName(dto.getNickName());
		}

		if (dto.getImgUrl() != null) {
			user.updateImgUrl(dto.getImgUrl());
		}

		if (dto.getPhoneNumber() != null) {
			user.updatePhoneNumber(dto.getPhoneNumber());
		}
		AlertMessageEntity alertMessageEntity = alertMessageRepository.save(user.getId(),
			new AlertMessageEntity(USER_CHANGED_MESSAGE));
		AlertMessageLogEntity log = new AlertMessageLogEntity(user.getId(),
			USER_CHANGED_MESSAGE);
		AlertMessageLogEntity savedLog = alertMessageLogRepository.save(log);
		alertEvent.publisher(user.getId(), savedLog.getId(), alertMessageEntity);

		return UserResponseDto.from(user);
	}

	@Override
	@Transactional
	public void changePassword(Long id, ChangePasswordDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(id);

		if (bCryptPasswordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
			if (dto.getNewPassword().equals(dto.getPasswordConfirm())) {
				user.changePassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
			} else {
				throw new PasswordConfirmNotMatchedException();
			}
		} else {
			throw new CurrentPasswordNotMatchedException();
		}
		AlertMessageEntity alertMessageEntity = alertMessageRepository.save(
			user.getId(),
			new AlertMessageEntity(USER_PASSWORD_CHANGED_MESSAGE));
		AlertMessageLogEntity log = new AlertMessageLogEntity(user.getId(),
			USER_PASSWORD_CHANGED_MESSAGE);
		AlertMessageLogEntity savedLog = alertMessageLogRepository.save(log);
		alertEvent.publisher(user.getId(), savedLog.getId(), alertMessageEntity);
	}

	@Override
	public UserResponseDto updateImgUrl(Long id, UpdateUserImgUrlReqeustDto dto) {

		UserEntity user = userRepository.findByIdOrElseThrow(id);
		user.updateImgUrl(dto.getImgUrl());

		AlertMessageEntity alertMessageEntity = alertMessageRepository.save(
			user.getId(),
			new AlertMessageEntity(USER_CHANGED_MESSAGE));
		AlertMessageLogEntity log = new AlertMessageLogEntity(user.getId(),
			USER_CHANGED_MESSAGE);
		AlertMessageLogEntity savedLog = alertMessageLogRepository.save(log);
		alertEvent.publisher(user.getId(), savedLog.getId(), alertMessageEntity);

		return UserResponseDto.from(user);

	}

	@Override
	@Transactional
	public void deleteUser(Long id, DeleteUserRequestDto dto) {

		UserEntity user = userRepository.findByIdOrElseThrow(id);

		if (user.getPassword() == null) {
			user.delete();
		} else if (bCryptPasswordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
			user.delete();
		} else {
			throw new CurrentPasswordNotMatchedException();
		}

	}
}
