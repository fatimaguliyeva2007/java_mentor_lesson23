package com.example.matrix_java_mentor_2.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z]).+$",
            message = "Password must contain uppercase and lowercase letters"
    )
    private String newPassword;
}
