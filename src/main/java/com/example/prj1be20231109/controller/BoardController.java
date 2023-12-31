package com.example.prj1be20231109.controller;

import com.example.prj1be20231109.domain.Board;
import com.example.prj1be20231109.domain.Member;
import com.example.prj1be20231109.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // controller + responsebody
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {


    // spring 주입을 위해 final로 (R Args로 생성자통해서)
    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board,
                              @SessionAttribute(value = "login", required = false) Member login) {
            // SessionAttribute사용해서 login 객체 얻기 로그인 안했을시 null값

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(!service.vaildate(board)) {
            return ResponseEntity.badRequest().build();
        }

        if (service.save(board, login)) {
         return   ResponseEntity.ok().build();
        } else {
           return ResponseEntity.internalServerError().build();
        }
    }

    // /api/board/list?p=6
    @GetMapping("list")
    public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page) {
        // Map넘어가는 데이터들 (1. 예전 Board관련 boardList, 2. 페이지관련 list)
        // boardList, List<Board>
        // pageInfo, ....

        return service.list(page);

    }

    // {id}는 {}랑 PathVariable 조합으로 Integer id의 값을 받는다.
    // @GetMapping("id/{id}") --> 앞의 id는 그냥 id로 들어가고, {id}는 parameter의 id값으로 들어간것!
    @GetMapping("id/{id}")
    public Board get(@PathVariable Integer id) {
        return service.get(id);
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id,
                                 @SessionAttribute (value = "login", required = false) Member login) {
        // 로그인 안하면 401 오류
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401오류
        }

        // 권한 없으면(자기글 아니면 403 FORBIDDEN오류)
        if (!service.hasAccess(id, login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403오류
        }

        if (service.remove(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Board board,
                               @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        if (!service.hasAccess(board.getId(), login)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }

//        System.out.println("board = " + board);
        if (service.vaildate(board)) {


        if (service.update(board)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
