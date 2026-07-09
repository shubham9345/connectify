package com.connectify.demo.Repository;

import com.connectify.demo.Model.Followers;
import com.connectify.demo.Model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowersRepository extends JpaRepository<Followers, Long> {

    boolean existsByFromAndTo(
            UserInfo from,
            UserInfo to
    );

    Optional<Followers> findByFromAndTo(
            UserInfo from,
            UserInfo to
    );

    @Query("""
            SELECT f.from
            FROM Followers f
            WHERE f.to.id = :userId
            """)
    List<UserInfo> findFollowersByUserId(
            @Param("userId") Long userId
    );

    @Query("""
            SELECT f.to
            FROM Followers f
            WHERE f.from.id = :userId
            """)
    List<UserInfo> findFollowingByUserId(
            @Param("userId") Long userId
    );
}
