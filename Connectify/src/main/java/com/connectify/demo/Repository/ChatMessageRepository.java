package com.connectify.demo.Repository;


import com.connectify.demo.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);
    List<ChatMessage> findRoomIdBySender(String sender);
//    @Query("SELECT DISTINCT c.roomId FROM ChatMessage c WHERE c.sender = :sender")
//    List<String> findRoomIdBySender(@Param("sender") String sender);
//    ChatMessage findChatMessageByRoomId(String roomId);
    // Optionally with pagination:
    // Page<ChatMessageEntity> findByRoomIdOrderByTimestampAsc(String roomId, Pageable pageable)
    List<ChatMessage> findChatMessageByRoomId(String roomId);
}