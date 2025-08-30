package dev.projects.HelloWorld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//Representational State Transfer(REST)
public class HelloWorldController {
    @GetMapping("/p")
    String pk() {

        return "its praveen";

    }
}
