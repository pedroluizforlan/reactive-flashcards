package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.StudyRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.QuestionResponse;
import com.pedroluizforlan.rectiveflashcards.api.mapper.StudyMapper;
import com.pedroluizforlan.rectiveflashcards.domain.service.StudyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("studies")
@Slf4j
@AllArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final StudyMapper studyMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(@Valid @RequestBody final StudyRequest studyRequest){
        return studyService.start(studyMapper.toDocument(studyRequest))
                .doFirst(() -> log.info("==== Trying to create a study with follow request {}", studyRequest))
                .map(document -> studyMapper.toResponse(document.getLastQuestionPending()));
    }
}
