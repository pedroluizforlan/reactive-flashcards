package com.pedroluizforlan.rectiveflashcards.domain.service.query;

import com.pedroluizforlan.rectiveflashcards.core.DeckApiConfig;
import com.pedroluizforlan.rectiveflashcards.domain.dto.AuthDTO;
import com.pedroluizforlan.rectiveflashcards.domain.dto.DeckDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class DeckRestQueryService {

    private final WebClient webClient;
    private final DeckApiConfig deckApiConfig;
    private final Mono<AuthDTO> authCache;


    public DeckRestQueryService(final WebClient webClient, final DeckApiConfig deckApiConfig) {
        this.webClient = webClient;
        this.deckApiConfig = deckApiConfig;
        authCache = Mono.from(getAuth())
                .cache(auth -> Duration.ofSeconds(auth.expiresIn() - 5),
                        throwable -> Duration.ZERO,
                        () -> Duration.ZERO);
    }

    public Flux<DeckDTO> getDecks(){
        return authCache
                .flatMapMany(authDTO -> doGetDecks(authDTO.token()));
    }

    private Flux<DeckDTO> doGetDecks(final String token){
        return webClient.get()
                .uri(deckApiConfig.getAuthUri())
                .header("token", token)
                .retrieve()
                .bodyToFlux(DeckDTO.class);
    }

    private Mono<AuthDTO> getAuth(){
        return webClient.post()
                .uri(deckApiConfig.getAuthUri())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AuthDTO.class);
    }

}
