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

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryModelAssembler assembler;

    public CategoryController(CategoryService categoryService, CategoryModelAssembler assembler) {
        this.categoryService = categoryService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<CategoryResponseDto>> getAll() {
        return CollectionModel.of(categoryService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(CategoryController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CategoryResponseDto> getById(@PathVariable Long id) {
        return assembler.toModel(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<CategoryResponseDto>> create(@Valid @RequestBody CategoryRequestDto dto) {
        CategoryResponseDto saved = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @PutMapping("/{id}")
    public EntityModel<CategoryResponseDto> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto dto) {
        return assembler.toModel(categoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/books-count")
    public List<CategoryBooksCountDto> countBooksByCategory() {
        return categoryService.countBooksByCategory();
    }
}
