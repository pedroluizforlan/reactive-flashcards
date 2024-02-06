package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.exception.ReactiveFlashCardsException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final MethodNotAllowHandler methodNotAllowHandler;
    private final NotFoundHandler notFoundHandler;
    private final ConstraintViolationExceptionHandler constraintViolationExceptionHandler;
    private final WebExchangeBindExceptionHandler webExchangeBindExceptionHandler;
    private final ResponseStatusHandler responseStatusHandler;
    private final ReactiveFlashCardsExceptionHandler reactiveFlashCardsExceptionHandler;
    private final GenericHandler genericHandler;
    private final JsonProcessingExceptionHandler jsonProcessingExceptionHandler;
    @Override
    public Mono<Void> handle(final ServerWebExchange exchange,final Throwable ex) {
        return Mono
                .error(ex)
                .onErrorResume(MethodNotAllowedException.class, e-> methodNotAllowHandler.handlerException(exchange,e))
                .onErrorResume(NotFoundException.class,e -> notFoundHandler.handlerException(exchange,e))
                .onErrorResume(ConstraintViolationException.class, e-> constraintViolationExceptionHandler.handlerException(exchange,e))
                .onErrorResume(WebExchangeBindException.class,e -> webExchangeBindExceptionHandler.handlerException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> responseStatusHandler.handlerException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e-> reactiveFlashCardsExceptionHandler.handlerException(exchange, e))
                .onErrorResume(Exception.class, e-> genericHandler.handlerException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e-> jsonProcessingExceptionHandler.handlerException(exchange, e))
                .then();
    }

}
