package com.example.hhblogdevelop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardFileRequestDto {
    private Long id;
    private Long[] idArr;
    private String fileId;
}