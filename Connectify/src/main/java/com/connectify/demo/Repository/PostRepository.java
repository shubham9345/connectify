package com.connectify.demo.Repository;

import com.connectify.demo.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
//    Post FindById(Long postId);
}
