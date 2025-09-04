package dev.hafil.demospringboot.controller;

import dev.hafil.demospringboot.model.TodoTask;
import dev.hafil.demospringboot.model.User;
import dev.hafil.demospringboot.service.TodoTaskServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoTaskController {

    private final TodoTaskServices todoTaskServices;

    private User getCurrentUser(Authentication auth) {
        return (User) auth.getPrincipal();
    }

    @GetMapping
    public ResponseEntity<List<TodoTask>> getAllTasks() {
        return todoTaskServices.findAll();
    }

    @GetMapping("/")
    public ResponseEntity<List<TodoTask>> getAllTasksByUser(Authentication auth) {
        User user = getCurrentUser(auth);
        List<TodoTask> tasks = todoTaskServices.findAllByCreator(user.getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoTask> getTaskById(@PathVariable long id) {
        TodoTask task = todoTaskServices.findById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TodoTask> createTask(@Valid Authentication auth , @RequestBody Map<String, Object> request) {
        User user = getCurrentUser(auth);
        String taskName = request.get("taskName").toString();

        TodoTask task = todoTaskServices.add(user.getId(), taskName);
        return ResponseEntity.ok(task);

    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoTask> updateTask(Authentication auth, @PathVariable long id, @RequestBody TodoTask todoTask) {

        User user = getCurrentUser(auth);
        if(todoTaskServices.findById(id).getCreatedBy() != user.getId()) return ResponseEntity.badRequest().build();

        TodoTask updatedTask = todoTaskServices.update(id, todoTask);
        return ResponseEntity.ok(updatedTask);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask( Authentication auth, @PathVariable long id) {
        User user = getCurrentUser(auth);
        if(todoTaskServices.findById(id).getCreatedBy() != user.getId()) return ResponseEntity.badRequest().build();

        return todoTaskServices.delete(id);
    }
}