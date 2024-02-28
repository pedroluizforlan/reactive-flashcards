package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.document.Question;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyCard;
import com.pedroluizforlan.rectiveflashcards.domain.dto.QuestionDTO;
import com.pedroluizforlan.rectiveflashcards.domain.dto.StudyDTO;
import com.pedroluizforlan.rectiveflashcards.domain.exception.DeckInStudyException;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.mapper.MailMapper;
import com.pedroluizforlan.rectiveflashcards.domain.mapper.StudyDomainMapper;
import com.pedroluizforlan.rectiveflashcards.domain.document.Card;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import com.pedroluizforlan.rectiveflashcards.domain.repository.StudyRepository;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.DeckQueryService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.StudyQueryService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.DECK_IN_STUDY;
import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.STUDY_QUESTION_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {
    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyQueryService studyQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;
    private final MailService mailService;
    private final MailMapper mailMapper;

    public Mono<StudyDocument> start(final StudyDocument studyDocument) {
        return verifyStudy(studyDocument)
                .then(userQueryService.findById(studyDocument.userId()))
                .flatMap(user -> deckQueryService.findById(studyDocument.studyDeck().deckId()))
                .flatMap(deck -> fillDeckStudyCards(studyDocument, deck.cards()))
                .map(study -> study.toBuilder()
                        .question(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards()))
                        .build())
                .doFirst(() -> log.info("==== Generating a first random question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("==== A follow study was save {}", study));
    }

    private Mono<StudyDocument> fillDeckStudyCards(final StudyDocument studyDocument, final Set<Card> cards) {
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("==== Copying cards to new study"))
                .map(studyDomainMapper::toStudyCards)
                .collectList()
                .map(studyCards -> studyDocument.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> studyDocument.toBuilder().studyDeck(studyDeck).build());
    }

    private Mono<Void> verifyStudy(final StudyDocument studyDocument) {
        return studyQueryService.findPendingStudyByUserIdAndDeckId(studyDocument.userId(), studyDocument.studyDeck().deckId())
                .flatMap(study -> Mono.defer(() -> Mono.error(new DeckInStudyException(DECK_IN_STUDY
                        .params(studyDocument.userId(), studyDocument.studyDeck().deckId())
                        .getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }

    public Mono<StudyDocument> answer(final String id, final String answer) {
        return studyQueryService.findById(id) //VERIFICADO
                .flatMap(studyQueryService::verifyIfFinished) //VERIFICADO
                .map(study -> studyDomainMapper.answer(study, answer)) // VERIFICADO
                .zipWhen(this::getNextPossibilities)
                .map(tuple -> studyDomainMapper.toDTO(tuple.getT1(), tuple.getT2()))
                .flatMap(this::setNewQuestion)
                .map(studyDomainMapper::toDocument)
                .flatMap(studyRepository::save)
                .doFirst(() -> log.info("==== Saving answer and next question if have one"));

    }

    private Mono<List<String>> getNextPossibilities(final StudyDocument studyDocument) {
        return Flux.fromIterable(studyDocument.studyDeck().cards())
                .doFirst(() -> log.info("==== Getting question not used or questions without right answers"))
                .map(StudyCard::front)
                .filter(asks -> studyDocument.questions().stream()
                        .filter(Question::isCorrect)
                        .map(Question::asked)
                        .noneMatch(question -> question.equals(asks)))
                .collectList()
                .flatMap(asks -> removeLastAsk(asks, studyDocument.getLastAnsweredQuestion().asked()));
    }

    private Mono<List<String>> removeLastAsk(final List<String> asks, final String asked) {
        return Mono.just(asks)
                .doFirst(() -> log.info("==== Removing last asked question if it is not a last pending question in study"))
                .filter(ask -> ask.size() == 1)
                .switchIfEmpty(Mono.defer(() -> Mono.just(asks.stream()
                        .filter(ask -> !ask.equals(asked))
                        .collect(Collectors.toList()))));
    }

    private Mono<StudyDTO> setNewQuestion(final StudyDTO studyDTO) {
        return Mono.just(studyDTO.hasAnyAnswer())
                .filter(BooleanUtils::isTrue)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND
                        .params(studyDTO.id())
                        .getMessage()))))
                .flatMap(hasAnyAnswer -> generateNextQuestion(studyDTO))
                .map(questionDTO -> studyDTO.toBuilder()
                        .question(questionDTO)
                        .build())
                .onErrorResume(NotFoundException.class, e -> Mono.just(studyDTO)
                        .onTerminateDetach()
                        .doOnSuccess(this::notifyUser));
    }

    private Mono<QuestionDTO> generateNextQuestion(final StudyDTO studyDTO) {
        return Mono.just(studyDTO.remainAsks().get(new Random().nextInt(studyDTO.remainAsks().size())))
                .doFirst(() -> log.info("==== Selecting next random question"))
                .map(ask -> studyDTO.studyDeck()
                        .cards()
                        .stream()
                        .filter(card -> card.front().equals(ask))
                        .map(studyDomainMapper::toQuestion)
                        .findFirst()
                        .orElseThrow());
    }

    private void notifyUser(final StudyDTO studyDTO) {
        userQueryService.findById(studyDTO.userId())
                .zipWhen(userDocument -> deckQueryService.findById(studyDTO.studyDeck().deckId()))
                .map(tuple -> mailMapper.toDTO(studyDTO, tuple.getT2(), tuple.getT1()))
                .flatMap(mailService::send)
                .subscribe();

    }
}
