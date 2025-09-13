package dev.projects.HelloWorld.controller;

import dev.projects.HelloWorld.Dtos.TodoResponse;
import dev.projects.HelloWorld.Services.TodoService;
import dev.projects.HelloWorld.Services.UserService;
import dev.projects.HelloWorld.models.Todo;
import dev.projects.HelloWorld.models.User;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/todo") //grouping
public class TodoController {
    @Autowired
    private TodoService todoService;
    @Autowired
    private UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Todo retrived sucessfully "),
            @ApiResponse(responseCode = "404",description = "Todo not found")
    })

    @GetMapping("/get")
    ResponseEntity<TodoResponse> getTodoById(@RequestParam("todoId") long id) {
        try {
            TodoResponse todo = todoService.getTodoByIdAndUser(id);
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all todos for the current user
    @GetMapping("/get/all")
    ResponseEntity<List<TodoResponse>> getTodos() {
        List<TodoResponse> todos = todoService.getTodosByUser();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    //Request param
    @PostMapping("/create")
    ResponseEntity<TodoResponse> createTodo(@RequestBody  Todo todo){
            User user = userService.getCurrentUser();
            todo.setUser(user);
            return new ResponseEntity<>(todoService.createTodo(todo), HttpStatus.CREATED);

    }
    @PutMapping("/update")
    ResponseEntity<TodoResponse> putTodoById(@RequestBody Todo todo){
        TodoResponse updated = todoService.updateTodoForUser(todo);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteTodoById(@PathVariable long id){
        return new ResponseEntity<>(todoService.deleteTodoForUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<String> deleteAllTodo(){
        return new ResponseEntity<>(todoService.deleteAllTodoForUser(), HttpStatus.OK);
    }

    @GetMapping("/page")
    ResponseEntity<Page<TodoResponse>> getTodosPage(@RequestParam int no, @RequestParam int size){
        Page<TodoResponse> page = todoService.getTodosPageForUser(no, size);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


}
