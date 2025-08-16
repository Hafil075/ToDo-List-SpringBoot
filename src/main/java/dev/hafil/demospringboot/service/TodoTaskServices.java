package dev.hafil.demospringboot.service;

import dev.hafil.demospringboot.exception.ResourceNotFoundException;
import dev.hafil.demospringboot.model.TodoTask;
import dev.hafil.demospringboot.repository.TodoTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoTaskServices {
    private final TodoTaskRepository todoTaskRepository;
    private final UserService userService;

    public ResponseEntity<List<TodoTask>> findAll() {
        List<TodoTask> tasks = todoTaskRepository.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    public TodoTask findById(Long id) {
        return todoTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public List<TodoTask> findAllByCreator(long creatorId) {
        // Verify user exists
        userService.findUserById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));

        return todoTaskRepository.findByCreatedBy(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("No tasks found for user: " + creatorId));
    }

    public TodoTask add(long creatorId, String task) {
        // Verify user exists
        userService.findUserById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));

        TodoTask todoTask = new TodoTask();
        todoTask.setCreatedBy(creatorId);
        todoTask.setTaskName(task);
        todoTask.setCompleted(false); // Set default value

        return todoTaskRepository.save(todoTask);
    }

    public TodoTask update(long id, TodoTask todoTask) {
        TodoTask existingTask = todoTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        existingTask.setTaskName(todoTask.getTaskName());
        existingTask.setCompleted(todoTask.isCompleted());
        return todoTaskRepository.save(existingTask);
    }

    public ResponseEntity<Object> delete(long id) {
        if (!todoTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        todoTaskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}