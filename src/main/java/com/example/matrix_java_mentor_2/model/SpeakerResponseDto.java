package com.example.matrix_java_mentor_2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Speaker response model")
public class SpeakerResponseDto {

    @Schema(description = "Unique speaker ID", example = "1")
    private Long id;

    @Schema(description = "Speaker full name", example = "Fatima Guliyeva")
    private String fullName;

    @Schema(description = "Speaker email", example = "fatima@gmail.com")
    private String email;

    @Schema(description = "Speaker company", example = "Matrix")
    private String company;
}