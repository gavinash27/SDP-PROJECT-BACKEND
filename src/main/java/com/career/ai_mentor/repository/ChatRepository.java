package com.career.ai_mentor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.career.ai_mentor.model.ChatMessage;

public interface ChatRepository extends JpaRepository<ChatMessage, Integer> {
}
