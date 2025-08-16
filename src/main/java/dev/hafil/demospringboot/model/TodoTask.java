package dev.hafil.demospringboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long createdBy;

    @Column(nullable = false)
    private String taskName;

    @ColumnDefault("false")
    @Column(name = "is_completed")
    private boolean completed;
}