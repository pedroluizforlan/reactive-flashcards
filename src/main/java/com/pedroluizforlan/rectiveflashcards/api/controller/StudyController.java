package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.StudyRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.QuestionResponse;
import com.pedroluizforlan.rectiveflashcards.api.mapper.StudyMapper;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import com.pedroluizforlan.rectiveflashcards.domain.service.StudyService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.StudyQueryService;
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
    private final StudyQueryService studyQueryService;
    private final StudyMapper studyMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(@Valid @RequestBody final StudyRequest studyRequest){
        return studyService.start(studyMapper.toDocument(studyRequest))
                .doFirst(() -> log.info("==== Trying to create a study with follow request {}", studyRequest))
                .map(document -> studyMapper.toResponse(document.getLastQuestionPending(), document.id()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id){
        return studyQueryService.getLastPendingQuestion(id)
                .doFirst(()-> log.info("==== Trying to get a next question on study {}", id))
                .map(question -> studyMapper.toResponse(question, id));
    }
}
