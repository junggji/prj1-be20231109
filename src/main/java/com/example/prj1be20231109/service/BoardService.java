package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Board;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    // isAdmin 사용을 위해 injection
    private final MemberService memberService;

    // 주입을 위해 RArgs
    private final BoardMapper mapper;

    public boolean save(Board board, Member login) {
        board.setWriter(login.getId());

        return mapper.insert(board) == 1;
    }



    // 빈 값이 있으면 badRequest 응답 하는 코드-------------
    public boolean vaildate(Board board) {
        if(board == null) {
            return false;
        }
        if(board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        return true;
    }

    public List<Board> list() {
        return mapper.selectAll();
    }

    public Board get(Integer id) {
        return mapper.selectById(id);
    }

    public boolean remove(Integer id) {
        // 삭제코드
        return mapper.deleteById(id) == 1;
    }

    public boolean update(Board board) {
        return mapper.update(board) == 1;
    }

    public boolean hasAccess(Integer id, Member login) {
        if (memberService.isAdmin(login)) {
            return true;
        }
        Board board = mapper.selectById(id);

        // 로그인한 사용자와 글작성자의 id가 같은지
        return board.getWriter().equals(login.getId());
    }


}
