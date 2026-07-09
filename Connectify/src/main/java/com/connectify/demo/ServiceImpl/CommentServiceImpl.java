package com.connectify.demo.ServiceImpl;

import com.connectify.demo.Dto.CommentResponse;
import com.connectify.demo.Model.*;
import com.connectify.demo.Repository.CommentRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import com.connectify.demo.service.CommentService;
import com.connectify.demo.service.PostService;
import com.connectify.demo.service.UserInfoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserInfoService userInfoService;
    private final PostService postService;
    private final NotificationService notificationService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Comment addComments(
            String message,
            Long userId,
            Long postId
    ) {

        log.info(
                "Comment request received. UserId: {}, PostId: {}",
                userId,
                postId
        );

        UserInfo user =
                userInfoService.getUserbyId(userId);

        Post post =
                postService.getPostById(postId);

        post.setNoOfComments(
                post.getNoOfComments() + 1
        );

        List<Followers> followers =
                user.getFollowers();

        log.debug(
                "Sending comment notifications to {} followers",
                followers.size()
        );

        for (Followers follower : followers) {

            notificationService.sendNotification(
                    follower.getFrom(),
                    "post with postId "
                            + post.getPostId()
                            + " is commented by user "
                            + user.getName(),
                    userId,
                    post
            );
        }

        Comment comment = new Comment();

        comment.setMessage(message);
        comment.setUser(user);
        comment.setPost(post);
        comment.setTime(LocalDateTime.now());

        commentRepository.save(comment);

        log.info(
                "Comment added successfully. CommentId: {}, UserId: {}, PostId: {}",
                comment.getCommentId(),
                userId,
                postId
        );

        return comment;
    }

    @Override
    @Transactional
    public Page<CommentResponse> allCommentsByPostId(
            Long postId,
            int page,
            int size
    ) {

        log.info(
                "Fetching comments for postId: {}, page: {}, size: {}",
                postId,
                page,
                size
        );

        postService.getPostById(postId);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "time")
        );

        Page<CommentResponse> comments =
                commentRepository
                        .findByPost_PostId(
                                postId,
                                pageable
                        )
                        .map(comment -> new CommentResponse(
                                comment.getCommentId(),
                                comment.getMessage(),
                                comment.getUser().getUsername(),
                                comment.getTime()
                        ));

        log.info(
                "Fetched {} comments out of {} total comments for postId: {}",
                comments.getNumberOfElements(),
                comments.getTotalElements(),
                postId
        );

        return comments;
    }

    @Override
    public String deleteCommentById(Long commentId) {

        log.info(
                "Delete comment request received. CommentId: {}",
                commentId
        );

        if (!commentRepository.existsById(commentId)) {

            log.error(
                    "Comment not found. CommentId: {}",
                    commentId
            );

            throw new RuntimeException(
                    "comment is not found with id "
                            + commentId
            );
        }

        commentRepository.deleteById(commentId);

        log.info(
                "Comment deleted successfully. CommentId: {}",
                commentId
        );

        return "comment is deleted with Id - " + commentId;
    }

    @Override
    public int removeCommentByUserId(
            Long userId,
            Long postId,
            Long commentId
    ) {

        log.info(
                "Removing comment. UserId: {}, PostId: {}, CommentId: {}",
                userId,
                postId,
                commentId
        );

        Post post =
                postService.getPostById(postId);

        post.setNoOfComments(
                post.getNoOfComments() - 1
        );

        String jpql =
                "DELETE FROM Comment c " +
                        "WHERE c.user.id = :userId " +
                        "AND c.post.id = :postId " +
                        "AND c.commentId = :commentId";

        int deleted = entityManager.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .setParameter("commentId", commentId)
                .executeUpdate();

        log.info(
                "Comment removal completed. Deleted rows: {}",
                deleted
        );

        return deleted;
    }
}

