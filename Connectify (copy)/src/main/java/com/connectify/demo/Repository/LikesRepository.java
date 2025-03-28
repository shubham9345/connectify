package com.connectify.demo.Repository;

import com.connectify.demo.Model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Long> {
}
