package com.projet.MiniProjet.hateoas;

import com.projet.MiniProjet.controller.BorrowController;
import com.projet.MiniProjet.dto.BorrowResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BorrowModelAssembler implements RepresentationModelAssembler<BorrowResponseDto, EntityModel<BorrowResponseDto>> {
    @Override
    public EntityModel<BorrowResponseDto> toModel(BorrowResponseDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(BorrowController.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(BorrowController.class).getAll()).withRel("borrows"),
                linkTo(methodOn(BorrowController.class).borrowsCountByUser()).withRel("borrows-count-by-user")
        );
    }
}
