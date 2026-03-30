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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/borrows")
@Tag(name = "Borrows", description = "Gestion des emprunts")
public class BorrowController {

    private final BorrowService borrowService;
    private final BorrowModelAssembler assembler;

    public BorrowController(BorrowService borrowService, BorrowModelAssembler assembler) {
        this.borrowService = borrowService;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Lister tous les emprunts",
            description = "Retourne la liste complète des emprunts enregistrés."
    )
    @GetMapping
    public CollectionModel<EntityModel<BorrowResponseDto>> getAll() {
        return CollectionModel.of(
                borrowService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(BorrowController.class).getAll()).withSelfRel()
        );
    }

    @Operation(
            summary = "Récupérer un emprunt par son identifiant",
            description = "Retourne les informations d'un emprunt à partir de son identifiant."
    )
    @GetMapping("/{id}")
    public EntityModel<BorrowResponseDto> getById(
            @Parameter(description = "Identifiant de l'emprunt", example = "1")
            @PathVariable Long id
    ) {
        return assembler.toModel(borrowService.findById(id));
    }

    @Operation(
            summary = "Créer un emprunt",
            description = "Permet d'enregistrer un nouvel emprunt. Si le livre est disponible, sa quantité est diminuée."
    )
    @PostMapping
    public ResponseEntity<EntityModel<BorrowResponseDto>> create(@Valid @RequestBody BorrowRequestDto dto) {
        BorrowResponseDto saved = borrowService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
    }

    @Operation(
            summary = "Retourner un livre emprunté",
            description = "Marque un emprunt comme retourné et remet un exemplaire du livre en stock."
    )
    @PutMapping("/return/{id}")
    public EntityModel<BorrowResponseDto> returnBook(
            @Parameter(description = "Identifiant de l'emprunt à retourner", example = "1")
            @PathVariable Long id
    ) {
        return assembler.toModel(borrowService.returnBook(id));
    }

    @Operation(
            summary = "Supprimer un emprunt",
            description = "Supprime un emprunt à partir de son identifiant."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de l'emprunt", example = "1")
            @PathVariable Long id
    ) {
        borrowService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Compter les emprunts par utilisateur",
            description = "Retourne, pour chaque utilisateur, le nombre total d'emprunts effectués."
    )
    @GetMapping("/stats/by-user")
    public List<UserBorrowSummaryDto> borrowsCountByUser() {
        return borrowService.countBorrowsByUser();
    }
}