package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.UserRequestDto;
import com.example.matrix_java_mentor_2.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@Valid @RequestBody UserRequestDto dto) {
        userService.addUser(dto);
        return "User created";
    }
}
