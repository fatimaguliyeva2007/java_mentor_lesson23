package com.example.matrix_java_mentor_2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Request model for creating or updating a talk")
public class TalkRequestDto {

    @NotBlank
    @Schema(
            description = "Title of the talk",
            example = "Mastering Spring Boot 4",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @NotBlank
    @Schema(
            description = "Room where the talk takes place",
            example = "Room A",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String room;

    @NotNull
    @Positive
    @Schema(
            description = "Duration of the talk in minutes",
            example = "60",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer durationMinutes;

    @Schema(
            description = "Scheduled date and time of the talk",
            example = "2026-10-25T10:00:00"
    )
    private LocalDateTime scheduledAt;

    @NotNull
    @Positive
    @Schema(
            description = "ID of the speaker",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long speakerId;
}

