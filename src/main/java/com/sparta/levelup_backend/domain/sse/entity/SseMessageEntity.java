package com.sparta.levelup_backend.domain.sse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_sse_entity")
@NoArgsConstructor
public class SseMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private String message;

	public SseMessageEntity(Long userId, String message) {
		this.userId = userId;
		this.message = message;
	}
}
