package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.mapper.StudyDomainMapper;
import com.pedroluizforlan.rectiveflashcards.domain.document.Card;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import com.pedroluizforlan.rectiveflashcards.domain.repository.StudyRepository;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.DeckQueryService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {
    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;

    public Mono<StudyDocument> start(final StudyDocument studyDocument){
        return userQueryService.findById(studyDocument.userId())
                .flatMap(user -> deckQueryService.findById(studyDocument.studyDeck().deckId()))
                .flatMap(deck -> getCards(studyDocument, deck.cards()))
                .map(study -> study.toBuilder()
                        .addQuestion(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards()))
                        .build())
                .doFirst(() -> log.info("==== Generating a first random question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("==== A follow study was save {}", study));
    }

    public Mono<StudyDocument> getCards(final StudyDocument studyDocument, final Set<Card> cards){
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("==== Copying cards to new study"))
                .map(studyDomainMapper::toStudyCards)
                .collectList()
                .map(studyCards -> studyDocument.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> studyDocument.toBuilder().studyDeck(studyDeck).build());
    }

}
