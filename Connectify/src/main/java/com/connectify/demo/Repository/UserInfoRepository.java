package com.connectify.demo.Repository;

import com.connectify.demo.Model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
      UserInfo findByUsername(String Username);
}
