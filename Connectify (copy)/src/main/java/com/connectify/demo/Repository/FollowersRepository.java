package com.connectify.demo.Repository;

import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowersRepository extends JpaRepository<Followers, Long> {
    Optional<Followers> findByFromAndTo(UserInfo from, UserInfo to);

    boolean existsByFromAndTo(UserInfo from, UserInfo to);

    List<Followers> findByTo(UserInfo to);

    List<Followers> findByFrom(UserInfo from);
}
