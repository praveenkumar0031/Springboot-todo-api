package dev.projects.HelloWorld.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;



    @NotBlank(message = "Title cann't be Empty")
    String title;

    @NotBlank(message = "Description cannot be empty")
    String description;
    boolean done;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


}
