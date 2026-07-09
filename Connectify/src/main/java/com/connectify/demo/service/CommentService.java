package com.connectify.demo.service;

import com.connectify.demo.Dto.CommentResponse;
import com.connectify.demo.Model.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    Comment addComments(String message, Long userId, Long postId);

    public Page<CommentResponse> allCommentsByPostId(
            Long postId,
            int page,
            int size
    );

    String deleteCommentById(Long commentId);

    int removeCommentByUserId(
            Long userId,
            Long postId,
            Long commentId
    );
}
