package com.pedroluizforlan.rectiveflashcards.domain.exception;

public class RetryException extends ReactiveFlashCardsException{

    public RetryException(final String message, final Throwable cause){
        super(message, cause);
    }

}
