package dev.hafil.demospringboot.repository;

import dev.hafil.demospringboot.model.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {
   Optional<List<TodoTask>> findByCreatedBy(long createdBy);

}
