package com.sparta.levelup_backend.domain.auth.service;

import static com.sparta.levelup_backend.enums.ErrorCode.*;
import static com.sparta.levelup_backend.enums.ProviderType.NONE;

import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmailOrElseThrow(email);

        if (user.getIsDeleted()) {
            throw new UserException(ALREADY_DELETED_USER);
        }

        if (!user.getProvider().equals(NONE)) {
            throw new UserException(AUTH_TYPE_MISMATCH);
        }

        return new CustomUserDetails(user);
    }
}
