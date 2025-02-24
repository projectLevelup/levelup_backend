package com.sparta.levelup_backend.domain.sse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlertMessageEntity {

	@JsonProperty("message")
	private String message;
}
