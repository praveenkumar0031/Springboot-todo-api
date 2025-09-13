package dev.projects.HelloWorld.Services;

import dev.projects.HelloWorld.Dtos.TodoResponse;
import dev.projects.HelloWorld.Repository.TodoRepository;
import dev.projects.HelloWorld.Repository.UserRepository;
import dev.projects.HelloWorld.models.Todo;
import dev.projects.HelloWorld.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//Bean
@Service
@RequiredArgsConstructor
public class TodoService {
    @Autowired
    private TodoRepository todoRepo;
    private UserRepository userRepo;
    private UserService userService;
    public TodoService(TodoRepository todoRepository) {
        this.todoRepo = todoRepository;
    }

    @Autowired
    public TodoService(TodoRepository todoRepo, UserService userService) {
        this.todoRepo = todoRepo;
        this.userService = userService;
    }
    public TodoResponse createTodo(Todo todo) {
        User user = userService.getCurrentUser();
        todo.setUser(user);
        Todo saved = todoRepo.save(todo);
        return mapToResponse(saved);
    }

    public List<TodoResponse> getTodosByUser() {
        User user = userService.getCurrentUser();
        return todoRepo.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public TodoResponse getTodoByIdAndUser(Long id) {
        User user = userService.getCurrentUser();
        Todo todo = todoRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        return mapToResponse(todo);
    }
    public TodoResponse updateTodoForUser(Todo todo) {
        User user = userService.getCurrentUser();
        Todo existing = todoRepo.findByIdAndUser(todo.getId(), user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        existing.setTitle(todo.getTitle());
        existing.setDescription(todo.getDescription());
        existing.setDone(todo.getDone());
        Todo saved = todoRepo.save(existing);
        return mapToResponse(saved);
    }


    public String deleteTodoForUser(Long id) {
        User user = userService.getCurrentUser();
        Todo existing = todoRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todoRepo.delete(existing);
        return "Deleted successfully";
    }

    // Delete all todos for user
    public String deleteAllTodoForUser() {
        User user = userService.getCurrentUser();
        List<Todo> todos = todoRepo.findByUser(user);
        todoRepo.deleteAll(todos);
        return "All todos deleted for user";
    }
    public Page<TodoResponse> getTodosPageForUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userService.getCurrentUser();
        return todoRepo.findByUser(user, pageable)
                .map(this::mapToResponse);
    }


    public TodoResponse mapToResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getDone(),
                todo.getUser().getId(),
                todo.getUser().getEmail() // only expose email
        );
    }
}
