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