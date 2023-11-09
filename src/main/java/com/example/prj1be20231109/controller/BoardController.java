package com.example.prj1be20231109.controller;

import com.example.prj1be20231109.domain.Board;
import com.example.prj1be20231109.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // controller + responsebody
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {


    // spring 주입을 위해 final로 (R Args로 생성자통해서)
    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {


        if(!service.vaildate(board)) {
            return ResponseEntity.badRequest().build();
        }

        if (service.save(board)) {
         return   ResponseEntity.ok().build();
        } else {
           return ResponseEntity.internalServerError().build();
        }
    }
}
