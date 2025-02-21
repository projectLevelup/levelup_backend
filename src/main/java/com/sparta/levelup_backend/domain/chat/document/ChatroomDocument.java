package com.sparta.levelup_backend.domain.chat.document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Document(collection = "chatroom")
public class ChatroomDocument {

	@Id
	private String id;
	private String title;
	private List<Participant> participants;
	private String lastMessage;
	private Map<String, Integer> unreadMessages;

	@Builder.Default
	private Boolean isDeleted = false;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	public void updateDeleted() {
		isDeleted = true;
	}

	public void updateParticipants(List<Participant> participants) {
		this.participants = participants;
	}

}
