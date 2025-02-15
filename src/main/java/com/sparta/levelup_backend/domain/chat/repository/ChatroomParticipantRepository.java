package com.sparta.levelup_backend.domain.chat.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.chat.entity.ChatroomParticipantEntity;

public interface ChatroomParticipantRepository extends JpaRepository<ChatroomParticipantEntity, Long> {

	boolean existsByUserIdAndChatroomId(Long userId, Long chatroomId);

	void deleteByUserIdAndChatroomId(Long userId, Long chatroomId);


	List<ChatroomParticipantEntity> findAllByUserId(Long userId);

}
