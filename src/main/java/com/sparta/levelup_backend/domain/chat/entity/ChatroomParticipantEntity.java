package com.sparta.levelup_backend.domain.chat.entity;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "ChatroomParticipant")
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatroomParticipantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne
	UserEntity user;

	@JoinColumn(name = "chatroom_id")
	@ManyToOne
	ChatroomEntity chatroom;
}
