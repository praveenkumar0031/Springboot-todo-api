package dev.projects.HelloWorld.controller;

import dev.projects.HelloWorld.Services.TodoService;
import dev.projects.HelloWorld.models.Todo;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Todo retrived sucessfully "),
            @ApiResponse(responseCode = "404",description = "Todo not found")
    })
    @GetMapping("/get")
    ResponseEntity <Todo> getTodoById(@RequestParam("todoId") long id) {

        try {
            Todo createdtodo = todoService.getTodo(id);
        return new ResponseEntity<>(createdtodo, HttpStatus.OK);

        }catch (RuntimeException e){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }
    //path variable
    @GetMapping("/get/all")
    ResponseEntity<List<Todo>> getTodos(){
        return new ResponseEntity<List<Todo>>(todoService.getTodos(),HttpStatus.OK);
    }
    //Request param
    @PostMapping("/create")
    ResponseEntity<Todo> createTodo(@RequestBody  Todo todo){

            return new ResponseEntity<>(todoService.createTodo(todo), HttpStatus.CREATED);

    }
    @PutMapping("/update")
    ResponseEntity<Todo> putTodoById(@RequestBody Todo todo){

            return new ResponseEntity<>(todoService.updateTodo(todo), HttpStatus.OK);

    }
    @DeleteMapping("/delete/{id}")
    String deleteTodoById(@PathVariable long id){

        return todoService.deleteTodo(id);
    }
    @DeleteMapping("/delete/all")
    String deleteAllTodo(){
        return todoService.deleteAllTodo();

    }
    @GetMapping("/page")
    ResponseEntity<Page<Todo>> getTodosPage(@RequestParam int no,@RequestParam int size){
        return new ResponseEntity<>(todoService.getAllTodosPages(no, size), HttpStatus.OK);
    }

}
