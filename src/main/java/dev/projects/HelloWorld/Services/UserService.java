package dev.projects.HelloWorld.Services;



import dev.projects.HelloWorld.Repository.UserRepository;
import dev.projects.HelloWorld.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



//Bean
@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public User createTodo(User user) {
        return userRepo.save(user); //create or update
    }

    public User getUserById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

}

