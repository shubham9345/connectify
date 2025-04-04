package com.connectify.demo.Repository;

import com.connectify.demo.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage,Long> {
}
