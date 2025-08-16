package dev.hafil.demospringboot.service;

import dev.hafil.demospringboot.exception.ResourceNotFoundException;
import dev.hafil.demospringboot.model.TodoTask;
import dev.hafil.demospringboot.repository.TodoTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TodoTaskServices {
    private TodoTaskRepository todoTaskRepository;

    private UserService userService;

    public ResponseEntity<List<TodoTask>> findAll() {
        List<TodoTask> tasks = todoTaskRepository.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    public TodoTask findById(Long id) {
        return todoTaskRepository.findById(id).get();
    }

    public List<TodoTask> findAllByCreator(long creatorId) {
        userService.findUserById(creatorId);
        return todoTaskRepository.findByCreatedBy(creatorId).
                orElseThrow(() -> new ResourceNotFoundException("No Task found"));
    }

    public TodoTask add(long creatorId ,String task) {
        userService.findUserById(creatorId);

        TodoTask todoTask = new TodoTask();
        todoTask.setCreatedBy(creatorId);
        todoTask.setTaskName(task);

        return todoTaskRepository.save(todoTask);
    }

    public TodoTask update(long id, TodoTask todoTask) {
        TodoTask task = todoTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setTaskName(todoTask.getTaskName());
        task.setCompleted(todoTask.isCompleted());
        return todoTaskRepository.save(task);
    }

    public ResponseEntity<Object> delete(long id) {
        findById(id);
        todoTaskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
