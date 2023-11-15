package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    @Insert("""
        INSERT INTO comment (boardId, comment, memberId)
        VALUES (#{boardId}, #{comment}, #{memberId})
        """)
    int insert(Comment comment);
}
