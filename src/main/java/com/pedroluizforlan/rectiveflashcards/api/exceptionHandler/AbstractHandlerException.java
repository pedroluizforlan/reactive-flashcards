package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.ProblemResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractHandlerException<T extends Exception> {

    private final ObjectMapper objectMapper;

    abstract Mono<Void> handlerException(final ServerWebExchange exchange, final T ex);

    protected ProblemResponse buildError(final HttpStatus status, final String errorDescription) {
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    protected Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(problemResponse))));
    }

    protected void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode) {
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

}
