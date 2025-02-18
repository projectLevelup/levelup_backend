package com.sparta.levelup_backend.domain.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sparta.levelup_backend.domain.chat.document.ChatMessage;

public interface ChatMongoRepository extends MongoRepository<ChatMessage, String> {
	List<ChatMessage> findByChatroomId(Long chatroomId);
}
