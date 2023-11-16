package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Like;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;

    public void update(Like like, Member login) {
        like.setMemberId(login.getId());
        // 처음 좋아요를 누를 때 : insert
        // 다시 누르면 : delete

        // 지웠는데 없으면 좋아요 안한것이니 insert, 있으면 바로 delete
        int count = 0;

        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }
    }
}
