package com.example.hhblogdevelop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String refreshToken;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_name")
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