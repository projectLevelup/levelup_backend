package com.sparta.levelup_backend.domain.community.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "community")
public class CommunityDocument {

	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String title;

	@Field(type = FieldType.Long)
	private Long userId;

	@Field(type = FieldType.Text)
	private String userNickName;

	@Field(type = FieldType.Text)
	private String gameName;

	@Field(type = FieldType.Boolean)
	private Boolean isDeleted;

	@Builder
	public CommunityDocument(Long communityId, String title, Long userId,
		String userNickName,
		String gameName, Boolean isDeleted) {
		this.id = String.valueOf(communityId);
		this.title = title;
		this.userId = userId;
		this.userNickName = userNickName;
		this.gameName = gameName;
		this.isDeleted = isDeleted;
	}

	public static CommunityDocument from(CommunityEntity community) {
		return CommunityDocument.builder()
			.communityId(community.getId())
			.title(community.getTitle())
			.userId(community.getUser().getId())
			.userNickName(community.getUser().getNickName())
			.gameName(community.getGame().getName())
			.isDeleted(community.getIsDeleted())
			.build();
	}

	public void updateIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

}