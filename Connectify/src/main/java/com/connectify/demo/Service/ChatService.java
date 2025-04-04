package com.connectify.demo.Service;

import com.connectify.demo.Model.ChatMessage;
import com.connectify.demo.Repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService {

    private final ChatRepository chatMessageRepository;

    @Autowired
    public ChatService(ChatRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Processes and stores the chat message.
     */
    public ChatMessage saveAndProcessMessage(ChatMessage message) {
        // Add timestamp to the message before saving
        message.setTime(LocalDateTime.now());

        // Save the chat message to the database

        // You can perform additional processing here if needed
        return chatMessageRepository.save(message);
    }
}
