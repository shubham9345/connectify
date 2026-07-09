package com.connectify.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "comment",
        indexes = {
                @Index(
                        name = "idx_comment_post",
                        columnList = "postId"
                ),
                @Index(
                        name = "idx_comment_user",
                        columnList = "userId"
                ),
                @Index(
                        name = "idx_comment_time",
                        columnList = "time"
                ),
                @Index(
                        name = "idx_comment_post_time",
                        columnList = "postId,time"
                )
        }
)
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId")
    private UserInfo user;

}
