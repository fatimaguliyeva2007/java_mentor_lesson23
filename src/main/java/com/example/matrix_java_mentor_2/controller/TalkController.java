package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.TalkRequestDto;
import com.example.matrix_java_mentor_2.model.TalkResponseDto;
import com.example.matrix_java_mentor_2.service.impl.TalkService;
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
@RequestMapping("/api/talks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Talk", description = "Talk management APIs")
public class TalkController {

    private final TalkService talkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create talk",
            description = "Creates a new talk"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Talk created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Speaker not found")
    })
    public TalkResponseDto create(@Valid @RequestBody TalkRequestDto dto) {
        return talkService.create(dto);
    }

    @GetMapping
    @Operation(
            summary = "Find all talks",
            description = "Returns all available talks"
    )
    @ApiResponse(responseCode = "200", description = "Talks retrieved successfully")
    public List<TalkResponseDto> findAll() {
        return talkService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find talk by ID",
            description = "Returns a talk using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talk found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Talk not found")
    })
    public TalkResponseDto findById(
            @Parameter(description = "Talk ID", example = "1")
            @PathVariable
            @Positive(message = "Id must be a positive number") Long id) {
        return talkService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update talk",
            description = "Updates an existing talk"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Talk updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Talk not found")
    })
    public TalkResponseDto update(
            @Parameter(description = "Talk ID", example = "1")
            @PathVariable
            @Positive(message = "Id must be a positive number") Long id,
            @Valid @RequestBody TalkRequestDto dto) {
        return talkService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete talk",
            description = "Deletes a talk using its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Talk deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Talk not found")
    })
    public void delete(
            @Parameter(description = "Talk ID", example = "1")
            @PathVariable
            @Positive(message = "Id must be a positive number") Long id) {
        talkService.delete(id);
    }
}
