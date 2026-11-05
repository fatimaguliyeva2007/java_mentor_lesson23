package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.SpeakerRequestDto;
import com.example.matrix_java_mentor_2.model.SpeakerResponseDto;
import com.example.matrix_java_mentor_2.model.TalkResponseDto;
import com.example.matrix_java_mentor_2.service.impl.SpeakerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.matrix_java_mentor_2.model.SpeakerRequestDto;
import com.example.matrix_java_mentor_2.model.SpeakerResponseDto;
import com.example.matrix_java_mentor_2.model.TalkResponseDto;
import com.example.matrix_java_mentor_2.service.impl.SpeakerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Speaker", description = "Speaker management APIs")
public class SpeakerController {

    private final SpeakerService speakerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create speaker",
            description = "Creates a new speaker"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Speaker created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Speaker already exists")
    })
    public SpeakerResponseDto create(@Valid @RequestBody SpeakerRequestDto dto) {
        return speakerService.create(dto);
    }

    @GetMapping
    @Operation(
            summary = "Find all speakers",
            description = "Returns all available speakers"
    )
    @ApiResponse(responseCode = "200", description = "Speakers retrieved successfully")
    public List<SpeakerResponseDto> findAll() {
        return speakerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find speaker by ID",
            description = "Returns a speaker using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Speaker found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Speaker not found")
    })
    public SpeakerResponseDto findById(
            @Parameter(description = "Speaker ID", example = "1")
            @PathVariable
            @Positive(message = "Id must be a positive number") Long id) {
        return speakerService.findById(id);
    }

    @GetMapping("/{id}/talks")
    @Operation(
            summary = "Find speaker talks",
            description = "Returns all talks belonging to a speaker"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Speaker not found")
    })
    public List<TalkResponseDto> findTalks(
            @Parameter(description = "Speaker ID", example = "1")
            @PathVariable
            @Positive(message = "Id must be a positive number") Long id) {
        return speakerService.findTalks(id);
    }
}
