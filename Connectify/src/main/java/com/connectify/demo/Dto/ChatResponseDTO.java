package com.connectify.demo.Dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatResponseDTO {
    private String sender;
    private String recipient;
    private String content;
    private String timestamp;
    private String roomId;

    public ChatResponseDTO() {
    }

    public ChatResponseDTO(String sender, String content, String timestamp,String recipient, String roomId) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.recipient = recipient;
        this.roomId = roomId;

    }

}

