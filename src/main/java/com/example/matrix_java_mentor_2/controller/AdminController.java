
package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.ChangeRolesRequestDto;
import com.example.matrix_java_mentor_2.model.UserResponseDto;
import com.example.matrix_java_mentor_2.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{id}/roles")
    public UserResponseDto changeUserRoles(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRolesRequestDto dto) {

        return userService.changeUserRoles(id, dto);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        userService.deleteUser(id, userDetails.getUsername());
    }
}
