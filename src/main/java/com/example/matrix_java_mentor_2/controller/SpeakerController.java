package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.SpeakerRequestDto;
import com.example.matrix_java_mentor_2.model.SpeakerResponseDto;
import com.example.matrix_java_mentor_2.model.SpeakerWithTalksRequestDto;
import com.example.matrix_java_mentor_2.model.TalkResponseDto;
import com.example.matrix_java_mentor_2.service.impl.ConferenceService;
import com.example.matrix_java_mentor_2.service.impl.SpeakerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
@Validated
public class SpeakerController {

    private final SpeakerService speakerService;
    private final ConferenceService conferenceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create speaker",
            description = "Create a new speaker",
            operationId = "createSpeaker"
    )
    public SpeakerResponseDto create(@Valid @RequestBody SpeakerRequestDto dto) {
        return speakerService.create(dto);
    }

    @PostMapping("/with-talks")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register speaker with talks",
            description = "Create a speaker and all of their talks in one transaction",
            operationId = "registerSpeakerWithTalks"
    )
    public SpeakerResponseDto registerWithTalks(@Valid @RequestBody SpeakerWithTalksRequestDto dto) {
        return conferenceService.registerSpeakerWithTalks(dto);
    }

    @GetMapping
    @Operation(
            summary = "Find all speakers",
            description = "Find all available speakers",
            operationId = "findSpeakers"
    )
    public List<SpeakerResponseDto> findAll() {
        return speakerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find speakers by id ",
            description = "Find available speakers by id ",
            operationId = "findByID"
    )
    public SpeakerResponseDto findById(@PathVariable @Positive(message = "Id must be a positive number") Long id) {
        return speakerService.findById(id);
    }

    @GetMapping("/{id}/talks")
    @Operation(
            summary = "Find talks from speakers",
            description = "Find available talks from speakers",
            operationId = "findTalks"
    )
    public List<TalkResponseDto> findTalks(@PathVariable @Positive(message = "Id must be a positive number") Long id) {
        return speakerService.findTalks(id);
    }
}
