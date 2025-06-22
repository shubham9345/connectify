package com.connectify.demo.Controller;

import Utility.ChatUtil;
import com.connectify.demo.Dto.ChatResponseDTO;
import com.connectify.demo.Model.ChatDisplay;
import com.connectify.demo.Model.ChatMessage;
import com.connectify.demo.Repository.ChatMessageRepository;
import com.connectify.demo.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    private final ChatMessageRepository chatMessageRepository;
    @Autowired
    public ChatService chatService;

    @Autowired
    public ChatHistoryController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/history/{otherUser}")
    public List<ChatResponseDTO> getHistory(@PathVariable String otherUser, Principal principal) {
        String me = principal.getName();
        // Compute roomId same as in service
        String roomId = ChatUtil.roomId(me, otherUser);

        List<ChatMessage> entities = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);

        // Map to DTO
        return entities.stream()
                .map(e -> new ChatResponseDTO(
                        e.getSender(),
                        e.getContent(),
                        e.getTimestamp().toString(),
                        e.getRecipient(),
                        e.getRoomId()
                ))
                .collect(Collectors.toList());
    }
    @GetMapping("/history/sender/{otherUser}")
    public ResponseEntity<?> findAllChatMessageBySender(@PathVariable String otherUser){
        List<ChatMessage> allChatMessage = chatService.findAllChatMessage(otherUser);
        return new ResponseEntity<>(allChatMessage, HttpStatus.OK);
    }
    @GetMapping("/all-content/{roomId}")
    public ResponseEntity<?> allContent(@PathVariable String roomId){
      List<ChatDisplay> allChat = chatService.allContentByRoomId(roomId);
      return new ResponseEntity<>(allChat,HttpStatus.OK);
    }
    @GetMapping("/all-roomid/{sender}")
    public Set<String> allRoomIdBySender(@PathVariable String sender){
        return chatService.allRoomId(sender);
    }
}
