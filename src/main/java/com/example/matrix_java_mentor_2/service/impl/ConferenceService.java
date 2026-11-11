package com.example.matrix_java_mentor_2.service.impl;

import com.example.matrix_java_mentor_2.model.SpeakerResponseDto;
import com.example.matrix_java_mentor_2.model.SpeakerWithTalksRequestDto;
import com.example.matrix_java_mentor_2.model.TalkRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConferenceService {

    private final SpeakerService speakerService;
    private final TalkService talkService;

    @Transactional
    public SpeakerResponseDto registerSpeakerWithTalks(SpeakerWithTalksRequestDto request) {
        SpeakerResponseDto speaker = speakerService.create(request.getSpeaker());

        for (TalkRequestDto talk : request.getTalks()) {
            talk.setSpeakerId(speaker.getId());
            talkService.create(talk);
        }

        log.info("Speaker {} registered with {} talks", speaker.getId(), request.getTalks().size());
        return speaker;
    }
}
