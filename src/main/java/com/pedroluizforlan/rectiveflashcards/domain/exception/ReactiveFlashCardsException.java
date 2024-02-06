package com.pedroluizforlan.rectiveflashcards.domain.exception;



public class ReactiveFlashCardsException extends RuntimeException{
    public ReactiveFlashCardsException(String message) {
        super(message);
    }

    public ReactiveFlashCardsException(String message, Throwable cause){
        super(message, cause);
    }
}
