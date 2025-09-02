package dev.projects.HelloWorld.Repository;

import dev.projects.HelloWorld.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
//crud
@Component
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
