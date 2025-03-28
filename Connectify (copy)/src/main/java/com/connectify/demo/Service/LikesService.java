package com.connectify.demo.Service;

import com.connectify.demo.Model.Comment;
import com.connectify.demo.Model.Likes;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikesService {
    @Autowired
    private LikesRepository likeRepository;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private PostService postService;

    public Likes addLike(Long userId, Long postId) {
        UserInfo user = userInfoService.getUserbyId(userId);
        if (user == null) {
            throw new RuntimeException("User is not found with Id.");
        }
        Post post = postService.getPostById(postId);

        if (post == null) {
            throw new RuntimeException("post not found this id");
        }

        Likes likes = new Likes();
        likes.setPosts(post);
        likes.setUser(user);
        likes.setTime(LocalDateTime.now());
        likeRepository.save(likes);
        return likes;
    }
    public List<Likes> allLikesByPostId(Long postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            throw new RuntimeException("post not found this id");
        }
        return post.getLikes();
    }

    public String deleteLikesById(Long LikesId) {
        if (!likeRepository.existsById(LikesId)) {
            throw new RuntimeException("comment is not found with id" + LikesId);
        }
        likeRepository.deleteById(LikesId);
        return "comment is deleted with Id - " + LikesId;
    }
}
