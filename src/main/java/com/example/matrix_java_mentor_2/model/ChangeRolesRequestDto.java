
package com.example.matrix_java_mentor_2.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChangeRolesRequestDto {

    @NotEmpty(message = "Roles cannot be empty")
    private List<String> roles;
}
