package com.connectify.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserInfo")

public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String userBio;
    private String username;
    private String url;
    private String email;
    private String password;
    private String roles;
    private int noOfPost;
    private int noOfFollowers;
    private int noOfFollowing;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.ALL} ,orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "to",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Followers> followers;

    @OneToMany(mappedBy = "from" ,cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonIgnore
    private List<Followers> following;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonIgnore
    private List<Likes> likes;

}