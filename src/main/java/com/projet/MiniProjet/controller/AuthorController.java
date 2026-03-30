package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.dto.AuthorRequestDto;
import com.projet.MiniProjet.dto.AuthorResponseDto;
import com.projet.MiniProjet.hateoas.AuthorModelAssembler;
import com.projet.MiniProjet.service.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Gestion des auteurs")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorModelAssembler authorModelAssembler;

    public AuthorController(AuthorService authorService, AuthorModelAssembler authorModelAssembler) {
        this.authorService = authorService;
        this.authorModelAssembler = authorModelAssembler;
    }

    @Operation(
            summary = "Lister tous les auteurs",
            description = "Retourne la liste complète des auteurs enregistrés."
    )
    @GetMapping
    public CollectionModel<EntityModel<AuthorResponseDto>> getAll() {
        List<EntityModel<AuthorResponseDto>> authors = authorService.findAll()
                .stream()
                .map(authorModelAssembler::toModel)
                .toList();

        return CollectionModel.of(
                authors,
                linkTo(methodOn(AuthorController.class).getAll()).withSelfRel()
        );
    }

    
    // @GetMapping
    // public CollectionModel<EntityModel<AuthorResponseDto>> getAll() {
    //     List<AuthorResponseDto> authorDtos = authorService.findAll();
    //     List<EntityModel<AuthorResponseDto>> authors = new ArrayList<>();

    //     for (AuthorResponseDto authorDto : authorDtos) {
    //         authors.add(authorModelAssembler.toModel(authorDto));
    //     }

    //     return CollectionModel.of(
    //             authors,
    //             linkTo(methodOn(AuthorController.class).getAll()).withSelfRel()
    //     );
    // }

    @Operation(
            summary = "Récupérer un auteur par son identifiant",
            description = "Retourne les informations d'un auteur à partir de son identifiant."
    )
    @GetMapping("/{id}")
    public EntityModel<AuthorResponseDto> getById(
            @Parameter(description = "Identifiant de l'auteur", example = "1")
            @PathVariable Long id
    ) {
        AuthorResponseDto author = authorService.findById(id);
        return authorModelAssembler.toModel(author);
    }

    @Operation(
            summary = "Créer un auteur",
            description = "Permet d'ajouter un nouvel auteur dans la bibliothèque."
    )
    @PostMapping
    public ResponseEntity<EntityModel<AuthorResponseDto>> create(@Valid @RequestBody AuthorRequestDto dto) {
        AuthorResponseDto savedAuthor = authorService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorModelAssembler.toModel(savedAuthor));
    }

    @Operation(
            summary = "Modifier un auteur",
            description = "Met à jour les informations d'un auteur existant."
    )
    @PutMapping("/{id}")
    public EntityModel<AuthorResponseDto> update(
            @Parameter(description = "Identifiant de l'auteur", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        AuthorResponseDto updatedAuthor = authorService.update(id, dto);
        return authorModelAssembler.toModel(updatedAuthor);
    }

    @Operation(
            summary = "Supprimer un auteur",
            description = "Supprime un auteur à partir de son identifiant."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de l'auteur", example = "1")
            @PathVariable Long id
    ) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}