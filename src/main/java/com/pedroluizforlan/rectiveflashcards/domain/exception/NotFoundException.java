package com.pedroluizforlan.rectiveflashcards.domain.exception;

public class NotFoundException extends ReactiveFlashCardsException {
    public NotFoundException(String message) {
        super(message);
    }
}
