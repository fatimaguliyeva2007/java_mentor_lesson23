package com.example.matrix_java_mentor_2.service.impl;

import com.example.matrix_java_mentor_2.entity.SpeakerEntity;
import com.example.matrix_java_mentor_2.entity.TalkEntity;
import com.example.matrix_java_mentor_2.enums.TalkStatus;
import com.example.matrix_java_mentor_2.exception.BusinessRuleException;
import com.example.matrix_java_mentor_2.exception.ResourceNotFoundException;
import com.example.matrix_java_mentor_2.mapper.TalkMapper;
import com.example.matrix_java_mentor_2.model.TalkRequestDto;
import com.example.matrix_java_mentor_2.model.TalkResponseDto;
import com.example.matrix_java_mentor_2.repository.SpeakerRepository;
import com.example.matrix_java_mentor_2.repository.TalkRepository;
import com.example.matrix_java_mentor_2.service.spec.TalkSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;


@Slf4j
@Service
@RequiredArgsConstructor
public class TalkService {

    private final TalkRepository talkRepository;
    private final SpeakerRepository speakerRepository;
    private final TalkMapper talkMapper;

    private static final int MIN_DURATION = 15;
    private static final int MAX_DURATION = 90;
    private static final int MAX_TALKS_PER_SPEAKER = 3;
    private static final int MAX_PAGE_SIZE = 50;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("title", "room", "durationMinutes");

    @Transactional
    public TalkResponseDto create(TalkRequestDto requestDto) {
        SpeakerEntity speaker = speakerRepository
                .findById(requestDto.getSpeakerId()).orElseThrow(() ->
                        new ResourceNotFoundException("Speaker not found with id: " + requestDto.getSpeakerId()));

        validateDuration(requestDto.getDurationMinutes());
        long talkCount = talkRepository.countBySpeakerId(speaker.getId());

        if (talkCount >= MAX_TALKS_PER_SPEAKER) {
            throw new BusinessRuleException("A speaker cannot have more than 3 talks");
        }

        TalkEntity talk = talkMapper.toEntity(requestDto);
        talk.setSpeaker(speaker);
        talk.setStatus(TalkStatus.SCHEDULED);
        TalkEntity savedTalk = talkRepository.save(talk);
        log.info("Talk created successfully with id: {}", savedTalk.getId());
        return talkMapper.toDto(savedTalk);
    }

    @Transactional(readOnly = true)
    public List<TalkResponseDto> findAll() {
        log.debug("Fetching all talks");
        return talkMapper.toDtoList(talkRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TalkResponseDto findById(Long id) {
        log.debug("Fetching talk with id: {}", id);
        TalkEntity talk = talkRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Talk not found with id: " + id));
        return talkMapper.toDto(talk);
    }

    @Transactional(readOnly = true)
    public List<TalkResponseDto> findBySpeakerId(Long speakerId) {
        if (!speakerRepository.existsById(speakerId)) {
            throw new ResourceNotFoundException("Speaker not found with id: " + speakerId);
        }
        log.debug("Fetching talks for speaker with id: {}", speakerId);
        return talkMapper.toDtoList(talkRepository.findBySpeakerId(speakerId));
    }

    @Transactional
    public void delete(Long id) {
        TalkEntity talk = talkRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Talk not found with id: " + id));
        talkRepository.delete(talk);
        log.info("Talk deleted successfully with id: {}", id);
    }

    @Transactional
    public TalkResponseDto update(Long id, TalkRequestDto requestDto) {
        TalkEntity existingTalk = talkRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Talk not found with id: " + id));

        SpeakerEntity newSpeaker = speakerRepository
                .findById(requestDto.getSpeakerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Speaker not found with id: " + requestDto.getSpeakerId()));

        validateDuration(requestDto.getDurationMinutes());
        Long oldSpeakerId = existingTalk.getSpeaker().getId();
        Long newSpeakerId = newSpeaker.getId();

        if (!oldSpeakerId.equals(newSpeakerId)) {
            long talkCount = talkRepository.countBySpeakerId(newSpeakerId);
            if (talkCount >= MAX_TALKS_PER_SPEAKER) {
                throw new BusinessRuleException("A speaker cannot have more than 3 talks");
            }
        }

        talkMapper.updateEntity(requestDto, existingTalk);
        existingTalk.setSpeaker(newSpeaker);
        TalkEntity updatedTalk = talkRepository.save(existingTalk);
        log.info("Talk updated successfully with id: {}", id);
        return talkMapper.toDto(updatedTalk);
    }

    @Transactional(readOnly = true)
    public Page<TalkResponseDto> getAllTalks(int page, int size, String sortBy, String direction) {
        log.debug("Fetching talks: page={}, size={}, sortBy={}", page, size, sortBy);
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return talkRepository.findAll(pageable).map(talkMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TalkResponseDto> search(String title, String room,
                                        Integer minDuration, Integer maxDuration,
                                        int page, int size, String sortBy, String direction) {
        log.debug("Searching talks: title={}, room={}, min={}, max={}, page={}, size={}, sortBy={}",
                title, room, minDuration, maxDuration, page, size, sortBy);

        if (minDuration != null && maxDuration != null && minDuration > maxDuration) {
            log.warn("minDuration {} is greater than maxDuration {}", minDuration, maxDuration);
            throw new BusinessRuleException("minDuration cannot be greater than maxDuration");
        }

        Pageable pageable = buildPageable(page, size, sortBy, direction);

        Specification<TalkEntity> spec = Specification.allOf(
                TalkSpecifications.titleContains(title),
                TalkSpecifications.inRoom(room),
                TalkSpecifications.minDuration(minDuration),
                TalkSpecifications.maxDuration(maxDuration)
        );

        return talkRepository.findAll(spec, pageable).map(talkMapper::toDto);
    }

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        if (page < 0) {
            log.warn("Invalid page: {}", page);
            throw new BusinessRuleException("Page must be 0 or more");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            log.warn("Invalid size: {}", size);
            throw new BusinessRuleException("Size must be between 1 and 50");
        }
        if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            log.warn("Invalid sortBy: {}", sortBy);
            throw new BusinessRuleException("sortBy must be one of: title, room, durationMinutes");
        }
        if (direction == null
                || !(direction.equalsIgnoreCase("asc") || direction.equalsIgnoreCase("desc"))) {
            log.warn("Invalid direction: {}", direction);
            throw new BusinessRuleException("direction must be asc or desc");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }

    private void validateDuration(Integer durationMinutes) {
        if (durationMinutes == null || durationMinutes < MIN_DURATION || durationMinutes > MAX_DURATION) {
            throw new BusinessRuleException("Talk duration must be between 15 and 90 minutes");
        }
    }
}