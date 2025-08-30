package dev.projects.HelloWorld;

import dev.projects.HelloWorld.models.Todo;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/projects/{projectId}/todo") //grouping
public class TodoController {
    @Autowired
    private TodoService todoService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Todo retrived sucessfully "),
            @ApiResponse(responseCode = "400",description = "Todo not found")
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
    ResponseEntity<List<Todo>> getTodos(@PathVariable Long projectId) {
        return new ResponseEntity<List<Todo>>(todoService.getTodosByProject(projectId),HttpStatus.OK);
    }
    //Request param

    @PostMapping("/create")

    ResponseEntity<Todo> createTodo(@PathVariable Long projectId,  @RequestBody  Todo todo){

            return new ResponseEntity<>(todoService.createTodo(projectId,todo), HttpStatus.CREATED);

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
