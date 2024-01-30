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
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono
                .error(ex)
                .onErrorResume(MethodNotAllowedException.class, e-> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(NotFoundException.class,e -> handleNotFoundException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e-> handleConstraintViolationException(exchange,e))
                .onErrorResume(WebExchangeBindException.class,e -> handleWebExchangeBindException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> handleResponseStatusException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e-> handleReactiveFlashCardsException(exchange, e))
                .onErrorResume(Exception.class, e-> handleException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e-> handleJsonProcessingException(exchange,e))
                .then();
    }

    private Mono<Void> handleConstraintViolationException(final ServerWebExchange exchange, final ConstraintViolationException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                })
                .map(message -> buildError(HttpStatus.BAD_REQUEST, message))
                .flatMap(problemResponse -> buildParamErrorMessage(problemResponse,exception))
                .doFirst(()->log.error("==== ConstraintViolationException", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }
    private Mono<Void> handleNotFoundException(final ServerWebExchange exchange, final NotFoundException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.NOT_FOUND);
                    return exception.getMessage();
                })
                .map(message -> buildError(HttpStatus.NOT_FOUND, message))
                .doFirst(()->log.error("==== NotFoundException", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleWebExchangeBindException(final ServerWebExchange exchange, final WebExchangeBindException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                })
                .map(message -> buildError(HttpStatus.BAD_REQUEST, message))
                .flatMap(problemResponse -> buildParamErrorMessage(problemResponse,exception))
                .doFirst(()->log.error("==== WebExchangeBindException", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleResponseStatusException(final ServerWebExchange exchange, final ResponseStatusException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.NOT_FOUND);
                    return GENERIC_NOT_FOUND.getMessage();
                })
                .map(message -> buildError(HttpStatus.NOT_FOUND, message))
                .doFirst(()->log.error("==== ResponseStatusException", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange, final MethodNotAllowedException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.params(exchange.getRequest().getMethod().name()).getMessage();
                })
                .map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .doFirst(()->log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}] ",
                        exchange.getRequest().getMethod(),
                        exchange.getRequest().getPath().value(),
                        exception))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<Void> handleReactiveFlashCardsException(final ServerWebExchange exchange, final ReactiveFlashCardsException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                })
                .map(message -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== ReactiveFlashCardsException ", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange, final JsonProcessingException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                })
                .map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== JsonProcessingException: Failed to map exception for the request {} ", exchange.getRequest().getPath().value(), exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleException(final ServerWebExchange exchange, final Exception exception){
        return Mono.fromCallable(() -> {
            prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
            return GENERIC_EXCEPTION.getMessage();
        })
                .map(message -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== Exception ", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono
                        .fromCallable(() -> new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(problemResponse))));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode){
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    private ProblemResponse buildError(final HttpStatus stats, final String errorDescription){
        return ProblemResponse
                .builder()
                .status(stats.value())
                .errorDescription(errorDescription)
                .build();
    }

    private Mono<ProblemResponse> buildParamErrorMessage(final ProblemResponse response, final ConstraintViolationException exception){
        return Flux.fromIterable(exception.getConstraintViolations())
                .map(constraintViolation -> ErrorFieldResponse.builder()
                                    .name(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().toString())
                                    .message(constraintViolation.getMessage()).build())
                .collectList()
                .map(problemResponses -> response.toBuilder().fields(problemResponses).build());
    }

    private Mono<ProblemResponse> buildParamErrorMessage(final ProblemResponse response, final WebExchangeBindException exception){
        return Flux.fromIterable(exception.getAllErrors())
                .map(objectError -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError ? fieldError.getField(): objectError.getObjectName())
                        .message(messageSource.getMessage(objectError, LocaleContextHolder.getLocale())).build())
                .collectList()
                .map(problemResponses -> response.toBuilder().fields(problemResponses).build());
    }

}
