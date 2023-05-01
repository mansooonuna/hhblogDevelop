package com.example.hhblogdevelop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String refreshToken;
    @NotNull
    @OneToOne
    private Users user;

    public RefreshToken(String token, Users user) {
        this.refreshToken = token;
        this.user = user;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}