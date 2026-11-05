package com.example.matrix_java_mentor_2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request model for creating or updating a speaker")
public class SpeakerRequestDto {

    @NotBlank
    @Schema(
            description = "Full name of the speaker",
            example = "Fatima Guliyeva",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fullName;

    @Email
    @NotBlank
    @Schema(
            description = "Email address of the speaker",
            example = "fatima@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "Company where the speaker works",
            example = "Matrix",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String company;
}