package dev.projects.HelloWorld.controller;

import dev.projects.HelloWorld.Dtos.LoginRequest;
import dev.projects.HelloWorld.Repository.UserRepository;
import dev.projects.HelloWorld.Services.UserService;
import dev.projects.HelloWorld.models.User;
import dev.projects.HelloWorld.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
        String favourite = body.get("favourite");

        if(userRepo.findByEmail(email).isPresent()){
            return new ResponseEntity<String>("User already exists!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        else{
            userService.createUser(User.builder().email(email).password(password).favourite(favourite).build());
            return new ResponseEntity<String>("User registered successfully!", HttpStatus.CREATED);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest body){
        String email = body.email();
        String password = body.password();
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
    @PutMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String favourite = body.get("favourite");
        String newPassword = body.get("password");

        var found = userRepo.findByEmail(email);
        if(found.isEmpty()){
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }

        User user = found.get();
        if(!user.getFavourite().equalsIgnoreCase(favourite)){
            return new ResponseEntity<>("Favourite answer does not match!", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return new ResponseEntity<>("Password reset successful!", HttpStatus.OK);
    }

}
