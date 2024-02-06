package com.pedroluizforlan.rectiveflashcards.domain.service;


import com.pedroluizforlan.rectiveflashcards.domain.document.DeckDocument;
import com.pedroluizforlan.rectiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> save(final DeckDocument deckDocument){
        return deckRepository.save(deckDocument)
                .doFirst(() -> log.info("==== Try to save a follow deck {}", deckDocument));
    }
}
