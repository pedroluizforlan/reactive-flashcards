package com.pedroluizforlan.rectiveflashcards.domain.exception;

public class EmailAlreadyInUsedException extends ReactiveFlashCardsException{
    public EmailAlreadyInUsedException(final String message){
        super(message);

    }
}
