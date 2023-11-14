package com.example.prj1be20231109.controller;

import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody Member member) {
        if (service.validate(member)) {
            if(service.add(member)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "check", params = "id")
    public ResponseEntity checkId(String id) {
        if(service.getId(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(String email) {
        if (service.getEmail(email) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(String nickName) {
        if (service.getNickName(nickName) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("list")
    public List<Member> list() {
        return service.list();
    }

    @GetMapping
    public ResponseEntity<Member> view(String id) {
        // TODO : 로그인 했는 지? -> 안했으면 401
        // TODO : 자기 정보인지? -> 아니면 403
       Member member = service.getMember(id);

       return ResponseEntity.ok(member);
    }

    @DeleteMapping
    public ResponseEntity delete(String id) {
        // TODO : 로그인 했는 지? -> 안했으면 401
        // TODO : 자기 정보인지? -> 아니면 403

        if (service.deleteMember(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Member member) {
        // TODO: 로그인 했는지? 자기 정보인지?

        if(service.update(member)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody Member member, WebRequest request) {

        // WebRequest 메소드 중에 session관련 메소드있음(하나의 브라우저에서 온 요청을 공유하는)

        if (service.login(member, request)) {
            return ResponseEntity.ok().build();
        } else {
            // 권한 없음 코드 401 -> 로그인 안되어 권한없음
            //              403 -> 로그인 됐지만 권한없음
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
