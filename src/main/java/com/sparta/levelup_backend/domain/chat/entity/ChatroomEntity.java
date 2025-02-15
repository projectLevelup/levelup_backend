package com.sparta.levelup_backend.domain.chat.entity;

import java.util.HashSet;
import java.util.Set;

import com.sparta.levelup_backend.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "chatroom")
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatroomEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Builder.Default
	@Column(nullable = false)
	private Integer participantsCount = 0;

	@Builder.Default
	@OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ChatroomParticipantEntity> userSet = new HashSet<>();

	public void updateParticipantsCount(int participantsCount) {
		this.participantsCount = participantsCount;
	}

}