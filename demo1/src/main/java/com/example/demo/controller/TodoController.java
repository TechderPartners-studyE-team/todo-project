package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    // testTodo 메서드 작성하기
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService(); // Use test Service
        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    // POST Create
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // temporary user id

            // (1) TodoEntity 로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // (2) id를 null 로 초기화. 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);

            // (3) 임시 유저 아이디 설정  - 한 유저만 로그인 없이 사용 가능.. 인증, 인가 기능 없기 때문
            entity.setUserId(temporaryUserId);

            // (4) 서비스를 이용해 TodoEntity 생성
            List<TodoEntity> entities = service.create(entity);

            // (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO 를 이용해 ResponseDTO 를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (7) ResponseDTO 를 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // (8) 혹시 예외가 나는 경우 dto 대신 error 에 메세지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    // GET Retrieve
    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user"; // temporary user id.

        // (1) 서비스 메서드의 retrieve 메서드를 사용해 Todo 리스트를 가져옴.
        List<TodoEntity> entities = service.retrieve(temporaryUserId);

        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    // PUT Update
    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user"; // temporary user id.

        // (1) dto 를 entity 로 변환
        TodoEntity entity = TodoDTO.toEntity(dto);

        // (2) id 를 temporaryUserId 로 초기화
        entity.setUserId(temporaryUserId);

        // (3) 서비스를 이용해 entity 업데이트
        List<TodoEntity> entities = service.update(entity);

        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // (6) ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    // DELETE Delete
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";  // temporary user id.

            // (1) TodoEntity 로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // (2) 임시 유저 아이디 설정
            entity.setUserId(temporaryUserId);

            // (3) 서비스를 이용해 entity 삭제
            List<TodoEntity> entities = service.delete(entity);

            // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (6) ResponseDTO 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 혹시 예외가 나는 경우 dto 대신 error 메시지 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }
}
