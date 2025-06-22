package com.connectify.demo.Controller;

import Utility.ChatUtil;
import com.connectify.demo.Dto.ChatMessageDTO;
import com.connectify.demo.Service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Principal principal) {
        String sender = principal.getName();
        String recipient = chatMessage.getRecipient();
        String roomId = ChatUtil.roomId(sender, recipient);
        chatService.processAndSendMessage(sender, chatMessage,roomId);
    }
}
