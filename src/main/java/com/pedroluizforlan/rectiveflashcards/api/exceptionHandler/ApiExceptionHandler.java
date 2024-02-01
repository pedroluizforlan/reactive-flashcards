package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.ErrorFieldResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.ProblemResponse;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.exception.ReactiveFlashCardsException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.*;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, Throwable ex) {
        return Mono
                .error(ex)
                .onErrorResume(MethodNotAllowedException.class, e-> new MethodNotAllowHandler(objectMapper).handlerException(exchange,e))
                .onErrorResume(NotFoundException.class,e -> new NotFoundHandler(objectMapper).handlerException(exchange,e))
                .onErrorResume(ConstraintViolationException.class, e-> new ConstraintViolationExceptionHandler(objectMapper).handlerException(exchange,e))
                .onErrorResume(WebExchangeBindException.class,e -> new WebExchangeBindExceptionHandler(objectMapper, messageSource).handlerException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> new ResponseStatusExceptionHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e-> new ReactiveFlashCardsExceptionHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(Exception.class, e-> new GenericHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e-> new JsonProcessingExceptionHandler(objectMapper).handlerException(exchange, e))
                .then();
    }

}
