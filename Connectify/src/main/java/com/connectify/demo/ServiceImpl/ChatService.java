package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Dto.ChatMessageDTO;
import com.connectify.demo.Dto.ChatResponseDTO;
import com.connectify.demo.Model.ChatDisplay;
import com.connectify.demo.Model.ChatMessage;
import com.connectify.demo.Repository.ChatMessageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository messageRepository;

    public ChatService(SimpMessagingTemplate messagingTemplate,
                       ChatMessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void processAndSendMessage(
            String sender,
            ChatMessageDTO message,
            String roomId
    ) {

        String recipient = message.getRecipient();
        String content = message.getContent();
        Instant timestamp = Instant.now();

        log.info(
                "Processing chat message. Sender: {}, Recipient: {}, RoomId: {}",
                sender,
                recipient,
                roomId
        );

        ChatMessage entity = new ChatMessage(
                sender,
                recipient,
                content,
                timestamp,
                roomId
        );

        ChatMessage savedMessage =
                messageRepository.save(entity);

        ChatResponseDTO response =
                new ChatResponseDTO(
                        sender,
                        recipient,
                        content,
                        timestamp.toString(),
                        roomId
                );

        messagingTemplate.convertAndSend(
                "/topic/chat/" + roomId,
                response
        );

        log.info(
                "Message saved successfully. MessageId: {}",
                savedMessage.getId()
        );
    }
    public List<ChatMessage> findAllChatMessage(
            String sender
    ) {

        log.info(
                "Fetching chat messages for sender: {}",
                sender
        );

        return messageRepository.findAllChatMessageBySender(sender);
    }
    public List<ChatDisplay> allContentByRoomId(
            String roomId
    ) {

        log.info(
                "Fetching messages for roomId: {}",
                roomId
        );

        List<ChatMessage> messages =
                messageRepository.findChatMessageByRoomId(
                        roomId
                );

        List<ChatDisplay> response =
                messages.stream()
                        .map(chat ->
                                new ChatDisplay(
                                        chat.getSender(),
                                        chat.getContent(),
                                        roomId
                                )
                        )
                        .toList();

        log.info(
                "Total messages fetched: {}",
                response.size()
        );

        return response;
    }

    public Set<String> allRoomId(String username) {

        log.info(
                "Fetching all roomIds for user: {}",
                username
        );

        Set<String> roomIds =
                messageRepository.findAllRoomIdsByUsername(
                        username
                );

        log.info(
                "Total roomIds found: {}",
                roomIds.size()
        );

        return roomIds;
    }
}