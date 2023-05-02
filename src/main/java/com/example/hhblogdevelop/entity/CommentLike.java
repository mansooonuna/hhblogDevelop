package com.example.hhblogdevelop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_name")
    private Users user;

    @JsonManagedReference
    @Column
    private Long commentId;


    public CommentLike(Long commentId, Users user) {
        this.commentId = commentId;
        this.user = user;
    }
}
