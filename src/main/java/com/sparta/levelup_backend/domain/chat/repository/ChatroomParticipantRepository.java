package com.sparta.levelup_backend.domain.chat.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.chat.entity.ChatroomParticipantEntity;

public interface ChatroomParticipantRepository extends JpaRepository<ChatroomParticipantEntity, Long> {

}
