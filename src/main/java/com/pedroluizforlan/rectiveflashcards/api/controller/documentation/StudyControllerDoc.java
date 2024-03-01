package com.pedroluizforlan.rectiveflashcards.api.controller.documentation;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.AnswerQuestionRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.StudyRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.AnswerQuestionResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.QuestionResponse;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Study", description = "Endpoints para gerenciar estudos")
public interface StudyControllerDoc {

    @Operation(summary = "Inicia o estudo de um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "O estudo foi criado e retorna a primeira pergunta gerada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<QuestionResponse> start(@Valid @RequestBody StudyRequest studyRequest);

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna a ultima pergunta que não foi respondida",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current_question")
    Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable @MongoId(message = "{studyController.id}") String id);

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retorna a pergunta, a resposta fornecida e a resposta esperada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AnswerQuestionResponse.class))})
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "{id}/answer")
    Mono<AnswerQuestionResponse> answer(@Valid @PathVariable @MongoId(message = "{studyController.id}") String id,
                                        @Valid @RequestBody AnswerQuestionRequest request);
}
