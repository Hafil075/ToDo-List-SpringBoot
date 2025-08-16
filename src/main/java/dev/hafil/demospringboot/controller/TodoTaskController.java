package dev.hafil.demospringboot.controller;

import dev.hafil.demospringboot.model.TodoTask;
import dev.hafil.demospringboot.service.TodoTaskServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TodoTaskController {
    @Autowired
    private TodoTaskServices todoTaskServices;

    @GetMapping
    public List<TodoTask> getAllTasks() {
        return todoTaskServices.findAll().getBody();
    }

    @GetMapping("/getAllby")
    public List<TodoTask> getAllBy(@RequestParam long id) {
        return todoTaskServices.findAllByCreator(id);
    }

    @GetMapping("/getOne")
    public TodoTask getOne(@RequestParam long id) {
        return todoTaskServices.findById(id);
    }


//    @PostMapping("/add")
//    public TodoTask add(@RequestParam long creatorId, @RequestBody String task) {
//        return todoTaskServices.add(creatorId,task);
//    }
//
//    @PutMapping("/edit")
//    public TodoTask edit(@RequestParam long id, @RequestBody TodoTask todoTask) {
//        return todoTaskServices.update(id,todoTask);
//    }
//
//    @DeleteMapping("/remove")
//    public ResponseEntity<Object> remove(@RequestParam long id) {
//        return todoTaskServices.delete(id);
//    }

}
