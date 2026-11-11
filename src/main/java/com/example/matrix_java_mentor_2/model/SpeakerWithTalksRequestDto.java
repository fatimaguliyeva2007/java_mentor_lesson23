package com.example.matrix_java_mentor_2.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SpeakerWithTalksRequestDto {

    @NotNull(message = "Speaker is required")
    @Valid
    private SpeakerRequestDto speaker;

    @NotEmpty(message = "At least one talk is required")
    private List<TalkRequestDto> talks;
}
