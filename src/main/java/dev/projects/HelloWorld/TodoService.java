package dev.projects.HelloWorld;

import dev.projects.HelloWorld.models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Bean
@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(Todo todo) {
        return (todoRepository.save(todo)); //create or update
    }

    public Todo getTodo(long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List not found with id " + id));
    }
    public Page<Todo> getAllTodosPages(int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAll(pageable);
    }
    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }


    public Todo updateTodo(Todo updatedtodo){
        /*return todoRepository.findById(id)
                .map(existtodo -> {
                    existtodo.setTitle(updatedtodo.getTitle());
                    existtodo.setDescription(updatedtodo.getDescription());
                    return todoRepository.save(existtodo);
                })
                .orElseThrow(() -> new RuntimeException("List not found with id " + id));*/
        return todoRepository.save(updatedtodo);
    }
    public String deleteTodo(long id) {

        if(todoRepository.existsById(id)) {
            todoRepository.deleteById(id);

        } else {

            throw new RuntimeException("List not found with id " + id);
        }
        return "Deleted";

    }
    public String deleteAllTodo(){
        todoRepository.deleteAll();
        return "All lists are Deleted";
    }
}
