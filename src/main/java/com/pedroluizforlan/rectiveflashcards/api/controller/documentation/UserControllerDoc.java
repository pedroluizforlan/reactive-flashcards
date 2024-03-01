package com.pedroluizforlan.rectiveflashcards.api.controller.documentation;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserPageRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserPageResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserResponse;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Customer", description = "Endpoints para manipulação de usuários")
public interface UserControllerDoc {

    @Operation(summary = "Endpoint para criar um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar o usuário criado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
                    })
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<UserResponse> save(@Valid @RequestBody UserRequest userRequest);

    @Operation(summary = "Endpoint para buscar um usuário pelo identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o usuário correspondete ao identificador",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") String id);

    @Operation(summary = "Endpoint para buscar usuários de forma páginada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar os usuários de acordo com as informações passadas na request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserPageResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Mono<UserPageResponse> findOnDemand(@Valid UserPageRequest userPageRequest);

    @Operation(summary = "Endpoint de atualização de dados de usuários")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o usuário atualizado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") String id,
                              @Valid @RequestBody UserRequest userRequest);

    @Operation(summary = "Endpoint para deletar um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o usuário foi excluido")
    })
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id") String id);
}
