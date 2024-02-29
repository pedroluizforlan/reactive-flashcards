package com.pedroluizforlan.rectiveflashcards.domain.service;


import com.pedroluizforlan.rectiveflashcards.domain.document.DeckDocument;
import com.pedroluizforlan.rectiveflashcards.domain.mapper.DeckDomainMapper;
import com.pedroluizforlan.rectiveflashcards.domain.repository.DeckRepository;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.DeckQueryService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.DeckRestQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {


    private final DeckRepository deckRepository;
    private final DeckQueryService deckQueryService;
    private final DeckRestQueryService deckRestQueryService;
    private final DeckDomainMapper deckDomainMapper;

    public Mono<DeckDocument> save(final DeckDocument document){
        return deckRepository.save(document)
                .doFirst(() -> log.info("==== Try to save a follow deck {}", document));
    }

    public Mono<DeckDocument> update(final DeckDocument document){
        return deckQueryService.findById(document.id())
                .map(deck -> document.toBuilder()
                        .createdAt(document.createdAt())
                        .updatedAt(document.updatedAt())
                        .build())
                .flatMap(deckRepository::save)
                .doFirst(() -> log.info("==== Try to update a deck with follow info {}", document));
    }

    public Mono<Void> delete(final String id){
        return deckQueryService.findById(id)
                .flatMap(deckRepository::delete)
                .doFirst(() -> log.info("==== Try to delete a user with follow id {}", id));
    }


    public Mono<Void> sync(){
        return Mono.empty()
                .onTerminateDetach()
                .doOnSuccess(o -> backgroundSync())
                .then();
    }


    private void backgroundSync(){
        deckRestQueryService.getDecks()
                .map(deckDomainMapper::toDocument)
                .flatMap(deckRepository::save)
                .then()
                .subscribe();
    }

}
