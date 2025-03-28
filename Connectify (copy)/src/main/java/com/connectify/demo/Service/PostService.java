package com.connectify.demo.Service;

import com.connectify.demo.Model.Post;
import com.connectify.demo.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post addPost(Post post) {
        post.setTime(LocalDateTime.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Post getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new RuntimeException("post is found by Id - " + postId);
        }
        return post.get();
    }

    public String deletePostById(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("post is found found with Id- " + postId);
        }
        postRepository.deleteById(postId);
        return "post is deleted successfully";
    }
}
