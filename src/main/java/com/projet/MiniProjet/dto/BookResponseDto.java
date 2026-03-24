package com.projet.MiniProjet.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private Integer quantity;
    private String categoryName;
    private List<String> authors;
}