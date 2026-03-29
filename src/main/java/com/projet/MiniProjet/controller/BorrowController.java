package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.dto.BorrowRequestDto;
import com.projet.MiniProjet.dto.BorrowResponseDto;
import com.projet.MiniProjet.dto.UserBorrowSummaryDto;
import com.projet.MiniProjet.hateoas.BorrowModelAssembler;
import com.projet.MiniProjet.service.BorrowService;
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
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;
    private final BorrowModelAssembler assembler;

    public BorrowController(BorrowService borrowService, BorrowModelAssembler assembler) {
        this.borrowService = borrowService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<BorrowResponseDto>> getAll() {
        return CollectionModel.of(borrowService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(BorrowController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<BorrowResponseDto> getById(@PathVariable Long id) {
        return assembler.toModel(borrowService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<BorrowResponseDto>> create(@Valid @RequestBody BorrowRequestDto dto) {
        BorrowResponseDto saved = borrowService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @PutMapping("/return/{id}")
    public EntityModel<BorrowResponseDto> returnBook(@PathVariable Long id) {
        return assembler.toModel(borrowService.returnBook(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        borrowService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/by-user")
    public List<UserBorrowSummaryDto> borrowsCountByUser() {
        return borrowService.countBorrowsByUser();
    }
}
