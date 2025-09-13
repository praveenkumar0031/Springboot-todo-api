package dev.projects.HelloWorld.Repository;

import dev.projects.HelloWorld.models.Todo;
import dev.projects.HelloWorld.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

//crud
@Component
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);
    Optional<Todo> findByIdAndUser(Long id, User user);

    Page<Todo> findByUser(User user, Pageable pageable);
}
