package com.connectify.demo.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "chat_messages",
        indexes = {
                @Index(
                        name = "idx_chat_sender",
                        columnList = "sender"
                ),
                @Index(
                        name = "idx_chat_recipient",
                        columnList = "recipient"
                ),
                @Index(
                        name = "idx_chat_room",
                        columnList = "roomId"
                )
        }
)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient;
    private String roomId;
    @Column(columnDefinition = "TEXT")
    private String content;

    private Instant timestamp;

    // Constructors, getters, setters
    public ChatMessage() {}

    public ChatMessage(String sender, String recipient, String content, Instant timestamp, String roomId) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }

}
