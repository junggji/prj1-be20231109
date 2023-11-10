package com.example.prj1be20231109.mapper;

import com.example.prj1be20231109.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    @Insert("""
        INSERT INTO member (id, password, email)
        VALUES (#{id}, #{password}, #{email})
        """)
    int insert(Member member);
}
