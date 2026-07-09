package com.connectify.demo.Repository;

import com.connectify.demo.Model.Likes;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    boolean existsByPostsAndUser(Post posts, UserInfo user);
    Page<Likes> findByPosts_PostId(
            Long postId,
            Pageable pageable
    );
}
