package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.dto.BookRequestDto;
import com.projet.MiniProjet.dto.BookResponseDto;
import com.projet.MiniProjet.dto.CategoryBooksCountDto;
import com.projet.MiniProjet.hateoas.BookModelAssembler;
import com.projet.MiniProjet.service.BookService;
import com.projet.MiniProjet.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Gestion des livres")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final BookModelAssembler assembler;

    public BookController(BookService bookService, CategoryService categoryService, BookModelAssembler assembler) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Lister tous les livres",
            description = "Retourne la liste complète des livres enregistrés dans la bibliothèque."
    )
    @GetMapping
    public CollectionModel<EntityModel<BookResponseDto>> getAll() {
        return CollectionModel.of(
                bookService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(BookController.class).getAll()).withSelfRel()
        );
    }

    @Operation(
            summary = "Récupérer un livre par son identifiant",
            description = "Retourne les informations d'un livre à partir de son identifiant."
    )
    @GetMapping("/{id}")
    public EntityModel<BookResponseDto> getById(
            @Parameter(description = "Identifiant du livre", example = "1")
            @PathVariable Long id
    ) {
        return assembler.toModel(bookService.findById(id));
    }

    @Operation(
            summary = "Créer un livre",
            description = "Permet d'ajouter un nouveau livre dans la bibliothèque."
    )
    @PostMapping
    public ResponseEntity<EntityModel<BookResponseDto>> create(@Valid @RequestBody BookRequestDto dto) {
        BookResponseDto saved = bookService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(
            summary = "Modifier un livre",
            description = "Met à jour les informations d'un livre existant."
    )
    @PutMapping("/{id}")
    public EntityModel<BookResponseDto> update(
            @Parameter(description = "Identifiant du livre", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto dto
    ) {
        return assembler.toModel(bookService.update(id, dto));
    }

    @Operation(
            summary = "Supprimer un livre",
            description = "Supprime un livre à partir de son identifiant."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant du livre", example = "1")
            @PathVariable Long id
    ) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Compter les livres par catégorie",
            description = "Retourne, pour chaque catégorie, le nombre total de livres enregistrés."
    )
    @GetMapping("/stats/by-category")
    public List<CategoryBooksCountDto> booksCountByCategory() {
        return categoryService.countBooksByCategory();
    }
}