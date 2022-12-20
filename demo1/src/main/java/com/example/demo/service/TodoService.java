package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        // TodoEntity 저장
        repository.save(entity);
        // TodoEntity 검색
        TodoEntity saveEntity = repository.findById(entity.getId()).get();

        return saveEntity.getTitle();
    }

    // Create 구현
    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations
        validate(entity);

        repository.save(entity);

        log.info("Entity ID : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }
    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");

            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown User.");

            throw new RuntimeException("Unknown User.");
        }
    }

    // Retrieve 구현
    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    // Update 구현
    public List<TodoEntity> update(final TodoEntity entity) {
        // (1) 저장할 엔티티가 유효한 지 확인
        validate(entity);

        // (2) 넘겨받은 엔티티 id를 이용해 TodoEntity 를 가져옴. 존재하지 않는 엔티니는 업데이트 할 수 없기 때문
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            // (3) 변환된 TodoEntity 가 존재하면 값을 새 entity 의 값으로 덮어 씌움.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // (4) 데이터베이스에 새 값 저장
            repository.save(todo);
        });

        // Retrieve 메서드를 이용해 유저의 모든 TodoList 리턴
        return retrieve(entity.getUserId());
    }

    // Delete 구현
    public List<TodoEntity> delete(final TodoEntity entity) {
        // (1) 저장할 엔티티가 유효한 지 확인
        validate(entity);

        try {
            // (2) 엔티티 삭제
            repository.delete(entity);
        } catch (Exception e) {
            // (3) exception 발생 시 id 와 exception 로그
            log.error("Error deleting entity : ", entity.getId(), e);

            // (4) 컨트롤러로 exception 날림. 데이터베이스 내부 로직을 캡슐화 하기 위해 e 리턴 X -> 새 exception 오브젝트 리턴
            throw new RuntimeException("error deleting entity : " + entity.getId());
        }

        // (5) 새 TodoList 가져와 리턴
        return retrieve(entity.getUserId());
    }
}
