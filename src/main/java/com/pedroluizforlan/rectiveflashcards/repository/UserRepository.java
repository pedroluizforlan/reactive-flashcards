package com.pedroluizforlan.rectiveflashcards.repository;

import com.pedroluizforlan.rectiveflashcards.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
}
