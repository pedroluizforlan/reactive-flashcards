package com.pedroluizforlan.rectiveflashcards.domain.service.query;


import com.pedroluizforlan.rectiveflashcards.domain.document.Question;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.*;

@Service
@Slf4j
@AllArgsConstructor
public class StudyQueryService {
    private final StudyRepository studyRepository;

    public Mono<StudyDocument> findPendingStudyByUserIdAndDeckId(final String userId, final String deckId){
        return studyRepository.findByUserIdAndCompleteFalseAndStudyDeck_DeckId(userId,deckId)
                .doFirst(() -> log.info("==== Trying to get pending study with userId {} and deckId {}", userId, deckId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() ->  Mono.error(new NotFoundException(STUDY_DECK_NOT_FOUND.params(userId, deckId).getMessage()))));
    }

    public Mono<StudyDocument> findById(final String id){
        return studyRepository.findById(id)
                .doFirst(() -> log.info("==== Getting a study with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(()-> Mono.error(new NotFoundException(STUDY_NOT_FOUND.params(id).getMessage()))));
    }

    public Mono<Void> verifyIfFinished(final StudyDocument studyDocument){
        return Mono.just(studyDocument.complete())
                .filter(BooleanUtils::isFalse)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND.params(studyDocument.id()).getMessage()))))
                .then();
    }

    public Mono<Question> getLastPendingQuestion(final String id){
        return findById(id)
                .flatMap(studyDocument -> verifyIfFinished(studyDocument).thenReturn(studyDocument))
                .flatMapMany(studyDocument -> Flux.fromIterable(studyDocument.questionList()))
                .filter(Question::isAnswered)
                .doFirst(()-> log.info("==== Getting a current pending question in study {}", id))
                .single();
    }

}
