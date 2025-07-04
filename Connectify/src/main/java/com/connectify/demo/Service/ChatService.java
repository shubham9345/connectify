package com.connectify.demo.Service;

import com.connectify.demo.Dto.ChatMessageDTO;
import com.connectify.demo.Dto.ChatResponseDTO;
import com.connectify.demo.Model.ChatDisplay;
import com.connectify.demo.Model.ChatMessage;
import com.connectify.demo.Repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository messageRepository;

    public ChatService(SimpMessagingTemplate messagingTemplate,
                       ChatMessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    public void processAndSendMessage(String sender,ChatMessageDTO message,String roomId) {
        String recipient = message.getRecipient();
        String content = message.getContent();
        Instant timestamp = Instant.now();

        // 1. Save to database
        ChatMessage entity = new ChatMessage(sender, recipient, content, timestamp, roomId);
        messageRepository.save(entity);
        ChatResponseDTO response = new ChatResponseDTO(sender,recipient, content, timestamp.toString(),roomId);
        // Send to the topic for this room
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);
    }
    public List<ChatMessage> findAllChatMessage(String sender){
        //        List<ChatMessage> allChatMessageBySender = new ArrayList<>();
//        for(String ct:findAllRoomId){
//            ChatMessage cm = messageRepository.findChatMessageByRoomId(ct);
//            allChatMessageBySender.add(cm);
//        }
        return messageRepository.findRoomIdBySender(sender);
    }
    public List<ChatDisplay> allContentByRoomId(String roomId){
        List<ChatMessage> allChatMesage = messageRepository.findChatMessageByRoomId(roomId);
        List<ChatDisplay> allContent = new ArrayList<>();
        for(ChatMessage ch: allChatMesage){
            String content = ch.getContent();
            String sender = ch.getSender();
            ChatDisplay chatDisplay = new ChatDisplay();
            chatDisplay.setContent(content);
            chatDisplay.setSender(sender);
            chatDisplay.setRoomId(roomId);
            allContent.add(chatDisplay);
        }
        return allContent;
    }
    public Set<String> allRoomId(String sender){
        List<ChatMessage> allChatMessage = messageRepository.findRoomIdBySender(sender);
        List<ChatMessage> allChatMessageByRecipient = messageRepository.findRoomIdByrecipient(sender);
        Set<String> allRoomsId = new HashSet<>();
        for(ChatMessage cm : allChatMessage){
            String roomId = cm.getRoomId();
            allRoomsId.add(roomId);
        }
        for(ChatMessage cm : allChatMessageByRecipient){
            String roomId = cm.getRoomId();
            allRoomsId.add(roomId);
        }
        return allRoomsId;
    }

}