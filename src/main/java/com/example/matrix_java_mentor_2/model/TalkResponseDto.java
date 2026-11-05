package com.example.matrix_java_mentor_2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Talk response model")
public class TalkResponseDto {

    @Schema(description = "Unique talk ID", example = "1")
    private Long id;

    @Schema(description = "Talk title", example = "Mastering Spring Boot 4")
    private String title;

    @Schema(description = "Talk room", example = "Room A")
    private String room;

    @Schema(description = "Talk duration in minutes", example = "60")
    private Integer durationMinutes;

    @Schema(description = "Scheduled date and time", example = "2026-10-25T10:00:00")
    private LocalDateTime scheduledAt;

    @Schema(description = "Speaker ID", example = "1")
    private Long speakerId;

    @Schema(description = "Speaker name", example = "Fatima Guliyeva")
    private String speakerName;
}
