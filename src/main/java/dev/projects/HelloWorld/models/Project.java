package dev.projects.HelloWorld.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectName;
    @OneToMany(mappedBy="Project",cascade = CascadeType.ALL)
    private List<Todo> todos;
}
