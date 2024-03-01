package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.documentation.StudyControllerDoc;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.AnswerQuestionRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.StudyRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.AnswerQuestionResponse;
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
public class StudyController implements StudyControllerDoc {
    private final StudyService studyService;
    private final StudyQueryService studyQueryService;
    private final StudyMapper studyMapper;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(@Valid @RequestBody final StudyRequest studyRequest) {
        return studyService.start(studyMapper.toDocument(studyRequest))
                .doFirst(() -> log.info("==== Trying to create a study with follow request {}", studyRequest))
                .map(document -> studyMapper.toResponse(document.getLastPendingQuestion(), document.id()));
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current_question")
    public Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id) {
        return studyQueryService.getLastPendingQuestion(id)
                .doFirst(() -> log.info("==== Trying to get a next question on study {}", id))
                .map(question -> studyMapper.toResponse(question, id));
    }

    @Override
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "{id}/answer")
    public Mono<AnswerQuestionResponse> answer(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id,
                                               @Valid @RequestBody final AnswerQuestionRequest request) {
        return studyService.answer(id, request.answer())
                .doFirst(() -> log.info("==== Trying to answer pending question in study {} with {}", id, request.answer()))
                .map(studyDocument -> studyMapper.toResponse(studyDocument.getLastAnsweredQuestion())); //VERIFICADO!!
    }
}
