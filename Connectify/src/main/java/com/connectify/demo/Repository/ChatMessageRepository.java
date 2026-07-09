package com.connectify.demo.Repository;


import com.connectify.demo.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);
    List<ChatMessage> findRoomIdBySender(String sender);
    List<ChatMessage> findRoomIdByrecipient(String sender);
  @Query("""
           SELECT DISTINCT c.roomId
           FROM ChatMessage c
           WHERE c.sender = :username
              OR c.recipient = :username
           """)
  Set<String> findAllRoomIdsByUsername(
                    @Param("username") String username
            );
    List<ChatMessage> findChatMessageByRoomId(String roomId);

    List<ChatMessage> findAllChatMessageBySender(String sender);

}