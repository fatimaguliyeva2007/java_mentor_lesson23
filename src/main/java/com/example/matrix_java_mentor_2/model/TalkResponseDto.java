package com.example.matrix_java_mentor_2.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TalkResponseDto {
    private Long id;
    private String title;
    private String room;
    private Integer durationMinutes;
    private LocalDateTime scheduledAt;
    private Long speakerId;
    private String speakerName;
}
//id, title, room, durationMinutes, speakerId, speakerName
