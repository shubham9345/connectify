package com.connectify.demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserInfo")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String url;
    private String email;
    private String password;
    private String roles;
    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<UserInfo> followers = new ArrayList<>();
    @ManyToMany(mappedBy = "followers")
    private List<UserInfo> following = new ArrayList<>();
    @OneToMany(mappedBy = "user" ,cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.PERSIST})
    private List<Post> posts;
}