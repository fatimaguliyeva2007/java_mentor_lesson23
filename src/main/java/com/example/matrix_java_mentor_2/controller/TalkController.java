package com.example.matrix_java_mentor_2.controller;

import com.example.matrix_java_mentor_2.model.TalkRequestDto;
import com.example.matrix_java_mentor_2.model.TalkResponseDto;
import com.example.matrix_java_mentor_2.service.impl.TalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/talks")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Talk controller",
        description = "Manage all talks"
)
public class TalkController {

    private final TalkService talkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create talk",
            description = "Create a new talk",
            operationId = "createTalk"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Talk created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public TalkResponseDto create(@Valid @RequestBody TalkRequestDto dto) {
        return talkService.create(dto);
    }

    @GetMapping
    @Operation(
            summary = "Get all talks",
            description = "Get all talks with pagination and sorting",
            operationId = "getAllTalks"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Talks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
    })
    public Page<TalkResponseDto> getAllTalks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return talkService.getAllTalks(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get talk by ID",
            description = "Find a talk by its ID",
            operationId = "findTalkById"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Talk found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Talk not found")
    })
    public TalkResponseDto findById(@PathVariable @Positive(message = "Id must be a positive number") Long id) {

        return talkService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete talk",
            description = "Delete a talk by its ID",
            operationId = "deleteTalk"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Talk deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID"),
            @ApiResponse(responseCode = "404", description = "Talk not found")
    })
    public void delete(@PathVariable @Positive(message = "Id must be a positive number") Long id) {
        talkService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update talk",
            description = "Update an existing talk by its ID",
            operationId = "updateTalk"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Talk updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or ID"),
            @ApiResponse(responseCode = "404", description = "Talk not found")
    })
    public TalkResponseDto update(@PathVariable @Positive(message = "Id must be a positive number") Long id,
            @Valid @RequestBody TalkRequestDto dto) {

        return talkService.update(id, dto);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search talks",
            description = "Search talks with optional filters, pagination, and sorting",
            operationId = "searchTalks"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Talks retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search, pagination, or sorting parameters")
    })
    public Page<TalkResponseDto> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String room,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return talkService.search(title, room, minDuration, maxDuration, page, size, sortBy, direction);
    }
}