package com.sparta.levelup_backend.domain.sse.entity;

import com.sparta.levelup_backend.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "alertlog")
@Entity
@Getter
public class AlertMessageLogEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	Long userId;

	String message;

	boolean isSendSucess = false;

	public AlertMessageLogEntity(Long userId, String message) {
		this.userId = userId;
		this.message = message;
	}

	public void sended() {
		isSendSucess = true;
	}
}
