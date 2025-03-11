package com.sparta.levelup_backend.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sparta.levelup_backend.domain.chat.document.ChatMessage;

public interface ChatMongoRepository extends MongoRepository<ChatMessage, String> {

	Slice<ChatMessage> findMessagesByChatroomIdOrderByIdDesc(String chatroomId, Pageable pageable);
}
