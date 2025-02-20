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
import com.sparta.levelup_backend.exception.common.DuplicateException;

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

	@Test
	void 제목입력이_없을때_닉네임조합으로_제목생성() {
		//given
		Long userId = 1L;
		Long targetUserId = 2L;
		String title = "";
		String nicknameTitle = "Publisher, Subscriber";

		ChatroomDocument chatroomDocument = ChatroomDocument.builder()
			.id("test")
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
			.title(nicknameTitle)
			.participants(Arrays.asList(participant1, participant2))
			.build();

		assertThat(actualResult)
			.usingRecursiveComparison()
			.isEqualTo(expectedResult);
		verify(chatroomMongoRepository, times(1)).save(any(ChatroomDocument.class));

	}

	@Test
	void 중복된_채팅방이_있을때_예외() {
		//given
		Long userId = 1L;
		Long targetUserId = 2L;
		String title = "title";

		//when
		when(chatroomMongoRepository.countByParticipantsUserIds(Arrays.asList(targetUserId, userId))).thenReturn(1L);

		//then
		assertThatThrownBy(() -> {
			chatroomService.createChatroom(userId, targetUserId, title);
		}).isInstanceOf(BadRequestException.class)
			.hasMessageContaining(DUPLICATE_CHATROOM.getMessage());
	}

	@Test
	void 채팅방_나가기_성공() {
		//given
		Long userId = 1L;
		String chatroomId = "test";

		ChatroomDocument chatroomDocument = ChatroomDocument.builder()
			.id(chatroomId)
			.participants(Arrays.asList(participant1, participant2))
			.lastMessage("")
			.unreadMessages(new HashMap<>())
			.isDeleted(false)
			.build();

		when(chatroomMongoRepository.findByIdOrThrow(chatroomId)).thenReturn(chatroomDocument);

		//when
		chatroomService.leaveChatroom(userId, chatroomId);

		//then
		ChatroomDocument expectedResult = ChatroomDocument.builder()
			.id(chatroomId)
			.participants(Arrays.asList(participant2))
			.lastMessage("")
			.unreadMessages(new HashMap<>())
			.isDeleted(true)
			.build();

		verify(chatroomMongoRepository, times(1)).save(argThat(save ->
			save.getId().equals(expectedResult.getId())
			&& save.getParticipants().equals(expectedResult.getParticipants())
			&& save.getLastMessage().equals(expectedResult.getLastMessage())
			&& save.getUnreadMessages().equals(expectedResult.getUnreadMessages())
			 && save.getIsDeleted().equals(expectedResult.getIsDeleted())
			));
	}

	@Test
	void 참여자가_아닌_유저일시_예외() {
		//given
		Long leaveUserId = 3L;
		String chatroomId = "test";

		ChatroomDocument chatroomDocument = ChatroomDocument.builder()
			.id(chatroomId)
			.participants(Arrays.asList(participant1, participant2))
			.lastMessage("")
			.unreadMessages(new HashMap<>())
			.isDeleted(false)
			.build();

		when(chatroomMongoRepository.findByIdOrThrow(chatroomId)).thenReturn(chatroomDocument);

		//when & then
		assertThatThrownBy(() -> {
			chatroomService.leaveChatroom(leaveUserId, chatroomId);
		}).isInstanceOf(DuplicateException.class)
			.hasMessageContaining(PARTICIPANT_ISDELETED.getMessage());

	}





}