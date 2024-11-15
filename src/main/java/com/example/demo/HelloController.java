package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Hello Spring Boot, Im Lê Phú Duy - KMA";
    }

    @GetMapping("/user")
    public String userPage() {
        return "You're user of page";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "You're admin of page";
    }

}
