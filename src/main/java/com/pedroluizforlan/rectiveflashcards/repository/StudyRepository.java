package com.pedroluizforlan.rectiveflashcards.repository;

import com.pedroluizforlan.rectiveflashcards.document.StudyDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends ReactiveMongoRepository<StudyDocument, String> {
}
