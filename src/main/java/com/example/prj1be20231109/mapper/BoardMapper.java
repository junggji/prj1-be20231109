package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    //insert의 경우 return int하는 경향
    @Insert("""
        INSERT INTO board (title, content, writer)
        VALUES (#{title}, #{content}, #{writer})
        """)
    int insert(Board board);


    // 댓글이 없는것도 나와야 하므로 LEFT JOIN으로 한다.
    @Select("""
        SELECT b.id,
               b.title,
               b.writer,
               m.nickName,
               b.inserted,
               COUNT(c.id) countComment
        FROM board b JOIN member m ON b.writer = m.id
                    LEFT JOIN comment c ON b.id = c.boardId
        GROUP BY b.id
        ORDER BY b.id DESC;
        """)
    List<Board> selectAll();


    @Select("""
        SELECT b.id,
                b.title,
                b.content,
                b.writer,
                m.nickName,
                b.inserted
        FROM board b JOIN member m ON b.writer = m.id
        WHERE b.id = #{id}
        """)
    Board selectById(Integer id);


    @Delete("""
        DELETE FROM board
        WHERE id = #{id}
        """)
    int deleteById(Integer id);


    @Update("""
        UPDATE board
        SET title = #{title},
            content = #{content},
            writer = #{writer}
        WHERE id = #{id} 
        """)
    int update(Board board);


    @Delete("""
        DELETE FROM board
        WHERE writer = #{writer}
        """)
    int deleteByWriter(String writer);

    @Select("""
        SELECT id
        FROM board
        WHERE writer = #{id}
        """)
    List<Integer> selectIdListByMemberId(String id);
}
