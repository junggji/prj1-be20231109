package com.example.prj1be20231109.domain;

import com.example.prj1be20231109.util.AppUtil;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Data
public class Board {
    private Integer id;
    private String title;
    private String content;
    private String writer;
    private String nickName;
    private LocalDateTime inserted;
    private Integer countComment;
    private Integer countLike;

    // property는 get메소드형식의 이름으로 넘어가기 때문에 get~~로 만듦
    public String getAgo() {
        return AppUtil.getAgo(inserted, LocalDateTime.now());
    }
}
