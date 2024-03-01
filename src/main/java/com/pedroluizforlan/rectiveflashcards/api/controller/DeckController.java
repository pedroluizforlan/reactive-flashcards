package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.documentation.DeckControllerDoc;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.DeckRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.DeckResponse;
import com.pedroluizforlan.rectiveflashcards.api.mapper.DeckMapper;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import com.pedroluizforlan.rectiveflashcards.domain.service.DeckService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.DeckQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController implements DeckControllerDoc {
    public final DeckService deckService;
    public final DeckMapper deckMapper;
    public final DeckQueryService deckQueryService;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest deckRequest) {
        return deckService.save(deckMapper.toDocument(deckRequest))
                .doFirst(() -> log.info("==== Saving a deck with follow data {}", deckRequest))
                .map(deckMapper::toResponse);
    }

    @Override
    @PostMapping(value = "sync")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> sync() {
        return deckService.sync();
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id) {
        return deckQueryService.findById(id)
                .doFirst(() -> log.info("==== Finding a deck with follow id {}", id))
                .map(deckMapper::toResponse);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<DeckResponse> findAll() {
        return deckQueryService.findAll()
                .doFirst(() -> log.info("==== Finding all decks"))
                .map(deckMapper::toResponse);
    }

    @Override
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> update(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id,
                                     @Valid @RequestBody final DeckRequest deckRequest) {
        return deckService.update(deckMapper.toDocument(deckRequest, id))
                .doFirst(() -> log.info("==== Updating a deck with follow info [body: {}, id: {}]", deckRequest, id))
                .map(deckMapper::toResponse);
    }

    @Override
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{deckController.id") final String id) {
        return deckService.delete(id)
                .doFirst(() -> log.info("Deleting a deck with follow id {}", id));
    }
}
