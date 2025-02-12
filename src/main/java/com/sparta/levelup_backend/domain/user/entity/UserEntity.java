package com.sparta.levelup_backend.domain.user.entity;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.utill.UserRole;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor( access = AccessLevel.PROTECTED)
@Table( name = "user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickName;

    @Lob
    @Column(nullable = true)
    private String img;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String phoneNumber;

}
