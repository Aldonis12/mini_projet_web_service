package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.dto.AuthorRequestDto;
import com.projet.MiniProjet.dto.AuthorResponseDto;
import com.projet.MiniProjet.hateoas.AuthorModelAssembler;
import com.projet.MiniProjet.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorModelAssembler assembler;

    public AuthorController(AuthorService authorService, AuthorModelAssembler assembler) {
        this.authorService = authorService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<AuthorResponseDto>> getAll() {
        return CollectionModel.of(authorService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(AuthorController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<AuthorResponseDto> getById(@PathVariable Long id) {
        return assembler.toModel(authorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<AuthorResponseDto>> create(@Valid @RequestBody AuthorRequestDto dto) {
        AuthorResponseDto saved = authorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @PutMapping("/{id}")
    public EntityModel<AuthorResponseDto> update(@PathVariable Long id, @Valid @RequestBody AuthorRequestDto dto) {
        return assembler.toModel(authorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
