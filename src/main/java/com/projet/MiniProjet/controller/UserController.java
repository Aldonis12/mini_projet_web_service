package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.hateoas.UserModelAssembler;
import com.projet.MiniProjet.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<com.projet.MiniProjet.dto.UserResponseDto>> getAll() {
        return CollectionModel.of(
                userService.findAll().stream().map(assembler::toModel).toList(),
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<com.projet.MiniProjet.dto.UserResponseDto> getById(@PathVariable Long id) {
        return assembler.toModel(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
