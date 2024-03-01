package com.pedroluizforlan.rectiveflashcards.api.controller.documentation;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.DeckRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.DeckResponse;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Deck", description = "Endpoints para manipulação de decks")
public interface DeckControllerDoc {
    @Operation(summary = "Endpoint para criar um novo deck")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar o deck criado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeckResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<DeckResponse> save(@Valid @RequestBody DeckRequest deckRequest);

    @Operation(summary = "Endpoint para buscar decks de uma api terceira")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "foram incluidos na base novos decks")
    })
    @PostMapping(value = "sync")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> sync();

    @Operation(summary = "Endpoint para buscar decks de uma api terceira")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "foram incluidos na base novos decks")
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(message = "{deckController.id}") String id);

    @Operation(summary = "Endpoint para buscar decks de uma api terceira")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "foram incluidos na base novos decks")
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Flux<DeckResponse> findAll();

    @Operation(summary = "Endpoint para atualizar um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "o deck foi atualizado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeckResponse.class))})
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<DeckResponse> update(@PathVariable @Valid @MongoId(message = "{deckController.id}") String id,
                              @Valid @RequestBody DeckRequest deckRequest);

    @Operation(summary = "Endpoint para excluir um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o deck foi excluido")
    })
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{deckController.id") String id);
}
