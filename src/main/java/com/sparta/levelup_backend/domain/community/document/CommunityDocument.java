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
@Document(indexName = "community", createIndex = true)
public class CommunityDocument {

	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String title;

	@Field(type = FieldType.Text)
	private String content;

	@Field(type = FieldType.Long)
	private Long userId;

	@Field(type = FieldType.Text)
	private String userEmail;

	@Field(type = FieldType.Text)
	private String gameName;

	@Field(type = FieldType.Keyword)
	private String gameGenre;

	@Field(type = FieldType.Boolean)
	private Boolean isDeleted;

	@Builder
	public CommunityDocument(Long communityId, String title, String content, Long userId, String userEmail,
		String gameName,
		String gameGenre, Boolean isDeleted) {
		this.id = String.valueOf(communityId);
		this.title = title;
		this.content = content;
		this.userId = userId;
		this.userEmail = userEmail;
		this.gameName = gameName;
		this.gameGenre = gameGenre;
		this.isDeleted = isDeleted;
	}

	public static CommunityDocument from(CommunityEntity community) {
		return new CommunityDocument().builder()
			.communityId(community.getId())
			.title(community.getTitle())
			.content(community.getContent())
			.userId(community.getUser().getId())
			.userEmail(community.getUser().getEmail())
			.gameName(community.getGame().getName())
			.gameGenre(community.getGame().getGenre())
			.isDeleted(community.getIsDeleted())
			.build();
	}

	public void updateIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateContent(String content) {
		this.content = content;
	}
}