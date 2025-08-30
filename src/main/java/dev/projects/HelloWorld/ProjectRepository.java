package dev.projects.HelloWorld;

import dev.projects.HelloWorld.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
