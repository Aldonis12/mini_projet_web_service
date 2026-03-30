package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.dto.CategoryBooksCountDto;
import com.projet.MiniProjet.dto.CategoryRequestDto;
import com.projet.MiniProjet.dto.CategoryResponseDto;
import com.projet.MiniProjet.hateoas.CategoryModelAssembler;
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
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Gestion des catégories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryModelAssembler assembler;

    public CategoryController(CategoryService categoryService, CategoryModelAssembler assembler) {
        this.categoryService = categoryService;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Lister toutes les catégories",
            description = "Retourne la liste complète des catégories enregistrées."
    )
    @GetMapping
    public CollectionModel<EntityModel<CategoryResponseDto>> getAll() {
        return CollectionModel.of(
                categoryService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(CategoryController.class).getAll()).withSelfRel()
        );
    }

    @Operation(
            summary = "Récupérer une catégorie par son identifiant",
            description = "Retourne les informations d'une catégorie à partir de son identifiant."
    )
    @GetMapping("/{id}")
    public EntityModel<CategoryResponseDto> getById(
            @Parameter(description = "Identifiant de la catégorie", example = "1")
            @PathVariable Long id
    ) {
        return assembler.toModel(categoryService.findById(id));
    }

    @Operation(
            summary = "Créer une catégorie",
            description = "Permet d'ajouter une nouvelle catégorie dans la bibliothèque."
    )
    @PostMapping
    public ResponseEntity<EntityModel<CategoryResponseDto>> create(@Valid @RequestBody CategoryRequestDto dto) {
        CategoryResponseDto saved = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(
            summary = "Modifier une catégorie",
            description = "Met à jour les informations d'une catégorie existante."
    )
    @PutMapping("/{id}")
    public EntityModel<CategoryResponseDto> update(
            @Parameter(description = "Identifiant de la catégorie", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto dto
    ) {
        return assembler.toModel(categoryService.update(id, dto));
    }

    @Operation(
            summary = "Supprimer une catégorie",
            description = "Supprime une catégorie à partir de son identifiant."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de la catégorie", example = "1")
            @PathVariable Long id
    ) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Compter les livres par catégorie",
            description = "Retourne, pour chaque catégorie, le nombre total de livres enregistrés."
    )
    @GetMapping("/stats/books-count")
    public List<CategoryBooksCountDto> countBooksByCategory() {
        return categoryService.countBooksByCategory();
    }
}
