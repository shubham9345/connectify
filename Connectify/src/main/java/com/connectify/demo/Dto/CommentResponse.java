package com.connectify.demo.Dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String message,
        String username,
        LocalDateTime time
) {
}