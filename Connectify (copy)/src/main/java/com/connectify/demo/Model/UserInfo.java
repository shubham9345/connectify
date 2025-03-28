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
    private String username;
    private String url;
    private String email;
    private String password;
    private String roles;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Post> posts;

    @OneToMany(mappedBy = "to")
    @JsonIgnore
    private List<Followers> followers;

    @OneToMany(mappedBy = "from")
    @JsonIgnore
    private List<Followers> following;
}