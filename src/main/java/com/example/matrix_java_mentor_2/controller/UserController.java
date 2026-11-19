package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.ChangePasswordRequestDto;
import com.example.matrix_java_mentor_2.model.UserRequestDto;
import com.example.matrix_java_mentor_2.model.UserResponseDto;
import com.example.matrix_java_mentor_2.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@Valid @RequestBody UserRequestDto dto) {
        userService.addUser(dto);
    }

    @GetMapping("/user/me")
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {

        return userService.getCurrentUser(userDetails.getUsername());
    }

    @PutMapping("/user/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDto dto) {

        userService.changePassword(userDetails.getUsername(), dto);
    }
}
