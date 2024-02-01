package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOW;

@Slf4j
@Component
public class MethodNotAllowHandler extends AbstractHandlerException<MethodNotAllowedException>{

    public MethodNotAllowHandler(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handlerException(final ServerWebExchange exchange, final MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.params(exchange.getRequest().getMethod().name()).getMessage();
                }).map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}] ",
                        exchange.getRequest().getMethod(),
                        exchange.getRequest().getPath().value(),
                        ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

}
