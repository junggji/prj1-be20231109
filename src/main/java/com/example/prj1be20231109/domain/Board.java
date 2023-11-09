package com.example.prj1be20231109.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Board {
    private String id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime inserted;
}
