package com.sparta.levelup_backend.domain.chat.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.levelup_backend.domain.chat.document.ChatroomDocument;
import com.sparta.levelup_backend.domain.chat.document.Participant;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomCreateResponseDto;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomMongoRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BadRequestException;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;

@ExtendWith(MockitoExtension.class)
class ChatroomServiceImplTest {

	@InjectMocks
	private ChatroomServiceImpl chatroomService;

	@Mock
	private ChatroomMongoRepository chatroomMongoRepository;

	@Mock
	private UserRepository userRepository;

	private final UserEntity publisher = UserEntity.builder()
		.id(1L)
		.nickName("Publisher")
		.build();

	private final UserEntity subscriber = UserEntity.builder()
		.id(2L)
		.nickName("Subscriber")
		.build();

	private final Participant participant1 = new Participant(publisher);
	private final Participant participant2 = new Participant(subscriber);

	@Test
	void 채팅방_생성_성공() {
		//given
		Long userId = 1L;
		Long targetUserId = 2L;
		String title = "title";

		ChatroomDocument chatroomDocument = ChatroomDocument.builder()
			.id("test")
			.title(title)
			.participants(Arrays.asList(participant1, participant2))
			.lastMessage("")
			.unreadMessages(new HashMap<>())
			.build();

		when(chatroomMongoRepository.countByParticipantsUserIds(Arrays.asList(targetUserId, userId))).thenReturn(0L);
		when(userRepository.findByIdOrElseThrow(userId)).thenReturn(publisher);
		when(userRepository.findByIdOrElseThrow(targetUserId)).thenReturn(subscriber);
		when(chatroomMongoRepository.save(any(ChatroomDocument.class))).thenReturn(chatroomDocument);

		//when
		ChatroomCreateResponseDto actualResult = chatroomService.createChatroom(userId, targetUserId, title);

		//then
		ChatroomCreateResponseDto expectedResult = ChatroomCreateResponseDto.builder()
			.chatroomId("test")
			.title(title)
			.participants(Arrays.asList(participant1, participant2))
			.build();

		assertThat(actualResult)
			.usingRecursiveComparison()
			.isEqualTo(expectedResult);
		verify(chatroomMongoRepository, times(1)).save(any(ChatroomDocument.class));

	}

	@Test
	void 자기자신과_채팅방_생성시_예외처리() {
		//given
		Long userId = 1L;
		Long targetUserId = 1L;
		String title = "title";

		//when & then
		assertThatThrownBy(() -> {
			chatroomService.createChatroom(userId, targetUserId, title);
		}).isInstanceOf(BadRequestException.class)
			.hasMessageContaining(INVALID_CHATROOM_CREATE.getMessage());
	}


}