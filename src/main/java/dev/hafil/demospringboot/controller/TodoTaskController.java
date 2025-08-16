package dev.hafil.demospringboot.controller;

import dev.hafil.demospringboot.model.TodoTask;
import dev.hafil.demospringboot.service.TodoTaskServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TodoTaskController {

    private final TodoTaskServices todoTaskServices;

    @GetMapping
    public ResponseEntity<List<TodoTask>> getAllTasks() {
        return todoTaskServices.findAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodoTask>> getAllTasksByUser(@PathVariable long userId) {
        List<TodoTask> tasks = todoTaskServices.findAllByCreator(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoTask> getTaskById(@PathVariable long id) {
        TodoTask task = todoTaskServices.findById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TodoTask> createTask(@RequestBody Map<String, Object> request) {
        long creatorId = Long.parseLong(request.get("creatorId").toString());
        String taskName = request.get("taskName").toString();

        TodoTask task = todoTaskServices.add(creatorId, taskName);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoTask> updateTask(@PathVariable long id, @RequestBody TodoTask todoTask) {
        TodoTask updatedTask = todoTaskServices.update(id, todoTask);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable long id) {
        return todoTaskServices.delete(id);
    }
}