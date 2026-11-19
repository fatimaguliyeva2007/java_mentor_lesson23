package com.example.matrix_java_mentor_2.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = "Username is required")
    @Size(
            min = 4,
            max = 20,
            message = "Username must be 4-20 characters"
    )
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(
            min = 6,
            max = 100,
            message = "Password must be 6-100 characters"
    )
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).+$",
            message = "Password must contain at least one uppercase and one lowercase letter"
    )
    private String password;

    private List<String> roles;
}
