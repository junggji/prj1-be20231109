package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Like;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;

    public Map<String, Object> update(Like like, Member login) {

        like.setMemberId(login.getId());
        // 처음 좋아요를 누를 때 : insert
        // 다시 누르면 : delete

        // 지웠는데 없으면 좋아요 안한것이니 insert, 있으면 바로 delete
        int count = 0;

        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }

        int countLike = mapper.countByBoardId(like.getBoardId());

        return Map.of("like", count == 1,
                "countLike", countLike);
    }

    public Map<String, Object> get(Integer boardId, Member login) {
        int countLike = mapper.countByBoardId(boardId);

        Like like = null;
        if (login != null) {
            like = mapper.selectByBoardIdAndMemberId(boardId, login.getId());
        }

        // boardid와 로그인id로 조회한 결과가 있으면 like의 상태가 true, 없으면 false
        return Map.of("like", like != null, "countLike", countLike);
    }
}
