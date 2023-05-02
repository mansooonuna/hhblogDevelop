package com.example.hhblogdevelop.entity;

import com.example.hhblogdevelop.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.example.hhblogdevelop.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

import static com.example.hhblogdevelop.exception.ErrorCode.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "user_name", nullable = false)
    private Users user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    @JsonBackReference
    private List<Comment> comments;

    @Column(name = "post_like")
    @ColumnDefault("0")
    private int post_like;

    public Post(PostRequestDto postRequestDto, Users user) {
        // 입력값 Validation
        if (user.getUsername() == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        if (postRequestDto.getTitle() == null || postRequestDto.getTitle().isEmpty()) {
            throw new CustomException(POST_TITLE_NOT_FOUND);
        }

        if (postRequestDto.getContent() == null || postRequestDto.getContent().isEmpty()) {
            throw new CustomException(POST_CONTENT_NOT_FOUND);
        }

        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void updateLike(boolean likeOrDislike) {
        this.post_like = likeOrDislike ? this.post_like + 1 : this.post_like - 1;
    }

}
