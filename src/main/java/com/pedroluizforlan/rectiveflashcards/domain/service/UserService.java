package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.document.UserDocument;
import com.pedroluizforlan.rectiveflashcards.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class UserService  {

    private final UserRepository userRepository;

    public Mono<UserDocument> save(final UserDocument userDocument){
        return userRepository.save(userDocument)
                .doFirst(() -> log.info("==== try to save a follow document {}", userDocument));
    }
}
