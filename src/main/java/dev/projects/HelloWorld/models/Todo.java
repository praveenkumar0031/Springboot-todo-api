package dev.projects.HelloWorld.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

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


}
