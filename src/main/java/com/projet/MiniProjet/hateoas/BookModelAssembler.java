package com.projet.MiniProjet.hateoas;

import com.projet.MiniProjet.controller.BookController;
import com.projet.MiniProjet.dto.BookResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<BookResponseDto, EntityModel<BookResponseDto>> {

    @Override
    public EntityModel<BookResponseDto> toModel(BookResponseDto dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(BookController.class).getById(dto.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).getAll()).withRel("books"),
                linkTo(methodOn(BookController.class).booksCountByCategory()).withRel("books-count-by-category")
        );
    }
}
