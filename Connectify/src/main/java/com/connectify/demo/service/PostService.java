package com.connectify.demo.service;

import com.connectify.demo.Model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Post addPost(Post post, Long userId);

    Page<Post> getAllPost(int page, int size);

    Post getPostById(Long postId);

    String deletePostById(Long postId);

    int removePostByUserId(Long userId, Long postId);

    List<Post> allPostByUserId(Long userId);

    Post updatePost(Post post, Long postId);
}