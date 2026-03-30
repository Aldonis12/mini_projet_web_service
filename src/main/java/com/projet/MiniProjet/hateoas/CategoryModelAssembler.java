package com.projet.MiniProjet.hateoas;

import com.projet.MiniProjet.controller.CategoryController;
import com.projet.MiniProjet.dto.CategoryResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<CategoryResponseDto, EntityModel<CategoryResponseDto>> {
    @Override
    public EntityModel<CategoryResponseDto> toModel(CategoryResponseDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CategoryController.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).getAll()).withRel("categories")
        );
    }
}
