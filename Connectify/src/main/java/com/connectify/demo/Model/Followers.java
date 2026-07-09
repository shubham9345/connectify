package com.connectify.demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(
        name = "followers",
        indexes = {
                @Index(
                        name = "idx_followers_from_user",
                        columnList = "from_user_fk"
                ),
                @Index(
                        name = "idx_followers_to_user",
                        columnList = "to_user_fk"
                ),
                @Index(
                        name = "idx_followers_from_to",
                        columnList = "from_user_fk,to_user_fk"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_follow_relation",
                        columnNames = {
                                "from_user_fk",
                                "to_user_fk"
                        }
                )
        }
)
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "from_user_fk")
    private UserInfo from;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "to_user_fk")
    private UserInfo to;

    public Followers() {
    }

    ;

    public Followers(UserInfo from, UserInfo to) {
        this.from = from;
        this.to = to;
    }
}