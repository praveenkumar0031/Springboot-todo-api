package dev.projects.HelloWorld.controller;

import dev.projects.HelloWorld.Repository.UserRepository;
import dev.projects.HelloWorld.Services.UserService;
import dev.projects.HelloWorld.models.User;
import dev.projects.HelloWorld.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = passwordEncoder.encode(body.get("password"));

        if(userRepo.findByEmail(email).isPresent()){
            return new ResponseEntity<String>("User already exists!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else{
            userService.createUser(User.builder().email(email).password(password).build());
            return new ResponseEntity<String>("User registered successfully!", HttpStatus.CREATED);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = body.get("password");
        var found = userRepo.findByEmail(email);
        if(found.isEmpty()){
            return new ResponseEntity<String>("User not found!", HttpStatus.NOT_FOUND);
        }
        User user = found.get();
        if(!passwordEncoder.matches(password,user.getPassword())){
            return new ResponseEntity<String>("Wrong password!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token",token));
    }
}
