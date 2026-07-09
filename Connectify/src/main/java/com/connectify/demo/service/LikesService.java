package com.connectify.demo.service;

import com.connectify.demo.Dto.LikeResponse;
import com.connectify.demo.Model.Likes;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LikesService {

    Likes addLike(Long userId, Long postId);

    Page<LikeResponse> allLikesByPostId(
            Long postId,
            int page,
            int size
    );

    String deleteLikesById(Long likesId);

    int removeLikeByUserId(Long userId, Long postId);
}