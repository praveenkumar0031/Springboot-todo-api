package dev.projects.HelloWorld.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String,String> body){

    }
    @PostMapping("/login")
    public String loginUser(@RequestBody Map<String,String> body){

    }
}
