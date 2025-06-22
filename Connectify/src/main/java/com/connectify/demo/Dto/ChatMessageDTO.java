package com.connectify.demo.Dto;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class ChatMessageDTO {
    private String recipient;
    private String content;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
    }

}
