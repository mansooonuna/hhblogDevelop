package com.example.hhblogdevelop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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
