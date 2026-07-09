package com.connectify.demo.Dto;

import java.time.LocalDateTime;

public record LikeResponse(
        Long likeId,
        String username,
        LocalDateTime time
) {
}