package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.domain.exception.EmailAlreadyInUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
public class EmailAlreadyInUsedHandler extends AbstractHandlerException<EmailAlreadyInUsedException> {

    public EmailAlreadyInUsedHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange, EmailAlreadyInUsedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return ex.getMessage();
                })
                .map(message -> buildError(BAD_REQUEST, message))
                .doFirst(()->log.error("==== EmailAlreadyInUsedHandler", ex))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }
}
