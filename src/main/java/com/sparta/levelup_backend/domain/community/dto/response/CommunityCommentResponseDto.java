package com.sparta.levelup_backend.domain.community.dto.response;

import java.util.List;

import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommunityCommentResponseDto {
	private final Long communityId;
	private final String title;
	private final String content;
	private final String author; //글을 생성한 사용자의 email
	private final String game; // 글이 포함된 game의 name;
	private final List<CommentResponseDto> comments;

	public static CommunityCommentResponseDto of(CommunityEntity community, List<CommentResponseDto> comments) {
		return new CommunityCommentResponseDto(community.getId(), community.getTitle(), community.getContent(),
			community.getUser().getEmail(), community.getGame().getName(), comments);
	}
}
