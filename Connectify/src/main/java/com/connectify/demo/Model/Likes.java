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
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post posts;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;
}
