package com.projet.MiniProjet.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookRequestDto {
    private String title;
    private String isbn;
    private Integer quantity;
    private Long categoryId;
    private List<Long> authorIds;
}