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

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final BookModelAssembler assembler;

    public BookController(BookService bookService, CategoryService categoryService, BookModelAssembler assembler) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<BookResponseDto>> getAll() {
        return CollectionModel.of(bookService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(BookController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<BookResponseDto> getById(@PathVariable Long id) {
        return assembler.toModel(bookService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<BookResponseDto>> create(@Valid @RequestBody BookRequestDto dto) {
        BookResponseDto saved = bookService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @PutMapping("/{id}")
    public EntityModel<BookResponseDto> update(@PathVariable Long id, @Valid @RequestBody BookRequestDto dto) {
        return assembler.toModel(bookService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/by-category")
    public List<CategoryBooksCountDto> booksCountByCategory() {
        return categoryService.countBooksByCategory();
    }
}
