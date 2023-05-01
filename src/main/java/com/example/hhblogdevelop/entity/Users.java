package com.example.hhblogdevelop.entity;

import com.example.hhblogdevelop.dto.PostRequestDto;
import com.example.hhblogdevelop.dto.UserRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> postList;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikeList;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikeList;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private RefreshToken refreshToken;

    public Users(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public void update(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
