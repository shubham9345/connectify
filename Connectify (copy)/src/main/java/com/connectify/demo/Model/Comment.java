package com.connectify.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String message;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "postId")
    @JsonIgnore
    private Post post;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserInfo user;

}
