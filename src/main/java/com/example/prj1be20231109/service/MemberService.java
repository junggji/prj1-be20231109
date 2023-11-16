package com.example.prj1be20231109.service;

import com.example.prj1be20231109.domain.Auth;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.mapper.BoardMapper;
import com.example.prj1be20231109.mapper.CommentMapper;
import com.example.prj1be20231109.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper mapper;
    private final BoardMapper boardMapper;

    private final CommentMapper commentMapper;
    private final BoardService boardService;

    public boolean add(Member member) {
        return mapper.insert(member) == 1;
    }

    public String getId(String id) {
        return mapper.selectId(id);
    }

    public String getEmail(String email) {
        return mapper.selectEmail(email);
    }

    public String getNickName(String nickName) {
        return mapper.selectNickName(nickName);
    }

    public boolean validate(Member member) {
        if (member == null) {
            return false;
        }

        if (member.getEmail().isBlank()) {
            return false;
        }
        if (member.getPassword().isBlank()) {
            return false;
        }
        if (member.getId().isBlank()) {
            return false;
        }
        if (member.getNickName().isBlank()) {
            return false;
        }
        return true;
    }

    public List<Member> list() {
        return mapper.selectAll();
    }

    public Member getMember(String id) {
        return mapper.selectById(id);
    }

    public boolean deleteMember(String id) {
        // 0. 이 멤버가 작성한 댓글 삭제
        commentMapper.deleteByMemberId(id);

        // 1. 이 멤버가 작성한 게시물 삭제

        // 이 멤버가 작성한 게시물 번호들 조회
        List<Integer> boardIdList = boardMapper.selectIdListByMemberId(id);
        // 게시물 번호들 loop 각 게시물 삭제(boardService.remove)
        boardIdList.forEach((boardId) -> boardService.remove(boardId));

        boardMapper.deleteByWriter(id);

        // 2. 이 멤버 삭제

        return mapper.deleteById(id) == 1;
    }

    public boolean update(Member member) {
//        Member oldMember = mapper.selectById(member.getId());
//
//        if (member.getPassword().equals("")) {
//            member.setPassword(oldMember.getPassword());
//        }

        return mapper.update(member) == 1;
    }


    public boolean login(Member member, WebRequest request) {
        Member dbMember = mapper.selectById(member.getId());

        // 아이디로 조회하여 dbmember에 넣고, dbMember의 password와 인자로 받은 member의 password 일치시 true
        if (dbMember != null) {
            if (dbMember.getPassword().equals(member.getPassword())) {
                List<Auth> auth = mapper.selectAuthById(member.getId());

                dbMember.setAuth(auth);

                // 위에 if문에서 이미 검증했으니 password 노출되지 안도로고 공란으로 놓기
                dbMember.setPassword("");
                // 로그인 성공시 쿠키를 세션에 넣는다. (login객체에 dbMember(로그인정보)가 저장이되었다!!!)
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return true;
            }
        }
        return false;
    }

    public boolean hasAcess(String id, Member login) {
        if (isAdmin(login)) {
            return true;
        }
        return login.getId().equals(id);
    }

    // Admin인지 확인하는 메소드
    public boolean isAdmin(Member login) {
        if (login.getAuth() != null) {
            return login.getAuth()
                    .stream()
                    .map(e -> e.getName())
                    .anyMatch(n -> n.equals("admin"));
        }
        return false;
    }
}
