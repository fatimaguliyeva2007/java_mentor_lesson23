package com.example.matrix_java_mentor_2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public/test")
    public String publicHello() {
        return "Public endpoint";
    }

    @GetMapping("/user/test")
    public String userHello() {
        return "Hello USER";
    }

    @GetMapping("/admin/test")
    public String adminHello() {
        return "Hello ADMIN";
    }
}
