package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.exception.DeckInStudyException;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.mapper.StudyDomainMapper;
import com.pedroluizforlan.rectiveflashcards.domain.document.Card;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import com.pedroluizforlan.rectiveflashcards.domain.repository.StudyRepository;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.DeckQueryService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.StudyQueryService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.DECK_IN_STUDY;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {
    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyQueryService studyQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;

    public Mono<StudyDocument> start(final StudyDocument studyDocument){
        return verifyStudy(studyDocument)
                .then(userQueryService.findById(studyDocument.userId()))
                .flatMap(user -> deckQueryService.findById(studyDocument.studyDeck().deckId()))
                .flatMap(deck -> fillDeckStudyCards(studyDocument, deck.cards()))
                .map(study -> study.toBuilder()
                        .addQuestion(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards()))
                        .build())
                .doFirst(() -> log.info("==== Generating a first random question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("==== A follow study was save {}", study));
    }

    private Mono<StudyDocument> fillDeckStudyCards(final StudyDocument studyDocument, final Set<Card> cards){
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("==== Copying cards to new study"))
                .map(studyDomainMapper::toStudyCards)
                .collectList()
                .map(studyCards -> studyDocument.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> studyDocument.toBuilder().studyDeck(studyDeck).build());
    }

    private Mono<Void> verifyStudy(final StudyDocument studyDocument){
        return studyQueryService.findPendingStudyByUserIdAndDeckId(studyDocument.userId(), studyDocument.studyDeck().deckId())
                .flatMap(study -> Mono.defer(() ->
                        Mono.error(new DeckInStudyException(DECK_IN_STUDY
                                .params(studyDocument.userId(), studyDocument.studyDeck().deckId())
                                .getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }

}
