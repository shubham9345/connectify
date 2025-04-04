package com.connectify.demo.Exceptions;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException{

    private final String localMessage;

    public PostNotFoundException(String message,String localMessage){
        super(message);
        this.localMessage = localMessage;
    }
    public PostNotFoundException(String message, Throwable cause, String localMessage) {
        super(message, cause);
        this.localMessage = localMessage;
    }
}
