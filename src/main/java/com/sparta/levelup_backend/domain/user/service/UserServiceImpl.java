package com.sparta.levelup_backend.domain.user.service;

import static com.sparta.levelup_backend.domain.user.dto.UserMessage.PASSWORD_RESET_CODE_PREFIX;
import static com.sparta.levelup_backend.domain.user.dto.UserMessage.PASSWORD_RESET_PREFIX;
import static com.sparta.levelup_backend.domain.user.dto.UserMessage.PASSWORD_RESET_SUBJECT;
import static com.sparta.levelup_backend.enums.ErrorCode.AUTH_TYPE_NOT_GENERAL;
import static com.sparta.levelup_backend.enums.ErrorCode.FORBIDDEN_ACCESS;
import static com.sparta.levelup_backend.enums.ErrorCode.INVALID_CURRENT_PASSWORD;
import static com.sparta.levelup_backend.enums.ErrorCode.INVALID_NICKNAME;
import static com.sparta.levelup_backend.enums.ErrorCode.INVALID_PASSWORD_CONFIRM;
import static com.sparta.levelup_backend.enums.ErrorCode.INVALID_RESETCODE;
import static com.sparta.levelup_backend.enums.ProviderType.NONE;

import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;
import com.sparta.levelup_backend.domain.email.event.EmailEventPublisher;
import com.sparta.levelup_backend.domain.user.dto.request.ChangePasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.request.ResetPasswordConfirmDto;
import com.sparta.levelup_backend.domain.user.dto.request.ResetPasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserImgUrlReqeustDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.user.UserException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailEventPublisher emailEventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserResponseDto findUserById(String role, Long id) {

        if (role.equals("ROLE_ADMIN")) {
            UserEntity user = userRepository.findByIdOrElseThrow(id);
            return UserResponseDto.from(user);
        }
        throw new UserException(FORBIDDEN_ACCESS);
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
        updateUserdata(user, dto);

        return UserResponseDto.from(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordDto dto) {
        UserEntity user = userRepository.findByIdOrElseThrow(id);

        if (!bCryptPasswordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new UserException(INVALID_CURRENT_PASSWORD);
        }

        if (!dto.getNewPassword().equals(dto.getPasswordConfirm())) {
            throw new UserException(INVALID_PASSWORD_CONFIRM);
        }
        user.changePassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));

    }

    @Override
    @Transactional
    public UserResponseDto updateImgUrl(Long id, UpdateUserImgUrlReqeustDto dto) {

        UserEntity user = userRepository.findByIdOrElseThrow(id);
        user.updateImgUrl(dto.getImgUrl());

        return UserResponseDto.from(user);

    }

    @Override
    @Transactional
    public void deleteUser(Long id, DeleteUserRequestDto dto) {

        UserEntity user = userRepository.findByIdOrElseThrow(id);

        if (user.getPassword() != null || !bCryptPasswordEncoder.matches(dto.getCurrentPassword(),
            user.getPassword())) {
            throw new UserException(INVALID_CURRENT_PASSWORD);
        }

        user.delete();

    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto dto) {
        UserEntity user = userRepository.findByEmailOrElseThrow(dto.getEmail());

        if (!user.getProvider().equals(NONE)) {
            throw new UserException(AUTH_TYPE_NOT_GENERAL);
        }

        if (!user.getNickName().equals(dto.getNickName())) {
            throw new UserException(INVALID_NICKNAME);
        }

        String passwordResetCode = UUID.randomUUID().toString();
        ValueOperations<String, Object> passwordResetCodes = redisTemplate.opsForValue();
        passwordResetCodes.set(PASSWORD_RESET_CODE_PREFIX + dto.getEmail(), passwordResetCode,
            Duration.ofSeconds(300));
        emailEventPublisher.publisher(
            new SendEmailDto(dto.getEmail(), PASSWORD_RESET_SUBJECT,
                PASSWORD_RESET_PREFIX + passwordResetCode));

    }

    @Override
    @Transactional
    public void resetPasswordConfirm(ResetPasswordConfirmDto dto) {

        ValueOperations<String, Object> passwordResetCodes = redisTemplate.opsForValue();
        String passwordResetCode = (String) passwordResetCodes.get(
            PASSWORD_RESET_CODE_PREFIX + dto.getEmail());
        UserEntity user = userRepository.findByEmailOrElseThrow(dto.getEmail());

        if (!dto.getNewPassword().equals(dto.getPasswordConfirm())) {
            throw new UserException(INVALID_PASSWORD_CONFIRM);
        }

        if (!dto.getResetCode().equals(passwordResetCode)) {
            throw new  UserException(INVALID_RESETCODE);
        }

        user.changePassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        passwordResetCodes.set(PASSWORD_RESET_CODE_PREFIX + dto.getEmail(),
            passwordResetCode,
            Duration.ofSeconds(1));
    }

    private void updateUserdata(UserEntity user, UpdateUserRequestDto dto) {

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

    }
}
