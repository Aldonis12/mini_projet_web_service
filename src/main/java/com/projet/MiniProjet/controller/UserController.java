package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.hateoas.UserModelAssembler;
import com.projet.MiniProjet.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Lister tous les utilisateurs",
            description = "Retourne la liste complète des utilisateurs enregistrés."
    )
    @GetMapping
    public CollectionModel<EntityModel<com.projet.MiniProjet.dto.UserResponseDto>> getAll() {
        return CollectionModel.of(
                userService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()
        );
    }

    @Operation(
            summary = "Récupérer un utilisateur par son identifiant",
            description = "Retourne les informations d'un utilisateur à partir de son identifiant."
    )
    @GetMapping("/{id}")
    public EntityModel<com.projet.MiniProjet.dto.UserResponseDto> getById(
            @Parameter(description = "Identifiant de l'utilisateur", example = "1")
            @PathVariable Long id
    ) {
        return assembler.toModel(userService.findById(id));
    }

    @Operation(
            summary = "Supprimer un utilisateur",
            description = "Supprime un utilisateur à partir de son identifiant."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de l'utilisateur", example = "2")
            @PathVariable Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}