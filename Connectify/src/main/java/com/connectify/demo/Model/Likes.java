package com.connectify.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "likes",
        indexes = {
                @Index(
                        name = "idx_like_post",
                        columnList = "post_id"
                ),
                @Index(
                        name = "idx_like_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_like_post_user",
                        columnList = "post_id,user_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_like_post_user",
                        columnNames = {
                                "post_id",
                                "user_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post posts;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;
}