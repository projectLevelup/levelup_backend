package com.sparta.levelup_backend.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;

public interface ChatroomRepository extends JpaRepository<ChatroomEntity, Long> {
}
