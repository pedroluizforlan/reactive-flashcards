package com.pedroluizforlan.rectiveflashcards.domain.service.query;

import com.pedroluizforlan.rectiveflashcards.domain.document.DeckDocument;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.DECK_NOT_FOUND;


@Service
@Slf4j
@AllArgsConstructor
public class DeckQueryService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> findById(final String id){
        return deckRepository.findById(id)
                .doFirst(()->log.info("==== Try to find user with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(
                        () -> Mono.error(
                                new NotFoundException(DECK_NOT_FOUND.params(id).getMessage()))));
    }
}
