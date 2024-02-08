package com.pedroluizforlan.rectiveflashcards.api.controller;

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
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {
    public final DeckService deckService;
    public final DeckMapper deckMapper;
    public final DeckQueryService deckQueryService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest deckRequest){
        return deckService.save(deckMapper.toDocument(deckRequest))
                .doFirst(()->log.info("==== Saving a deck with follow data {}", deckRequest))
                .map(deckMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id){
        return deckQueryService.findById(id)
                .doFirst(() -> log.info("==== Finding a deck with follow id {}", id))
                .map(deckMapper::toResponse);
    }


}
