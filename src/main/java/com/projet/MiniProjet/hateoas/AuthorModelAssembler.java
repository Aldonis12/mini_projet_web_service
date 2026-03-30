package com.projet.MiniProjet.hateoas;

import com.projet.MiniProjet.controller.AuthorController;
import com.projet.MiniProjet.dto.AuthorResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorModelAssembler implements RepresentationModelAssembler<AuthorResponseDto, EntityModel<AuthorResponseDto>> {
    @Override
    public EntityModel<AuthorResponseDto> toModel(AuthorResponseDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(AuthorController.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(AuthorController.class).getAll()).withRel("authors")
        );
    }
}
