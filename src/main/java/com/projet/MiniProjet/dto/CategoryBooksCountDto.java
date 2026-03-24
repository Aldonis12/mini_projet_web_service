package com.projet.MiniProjet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryBooksCountDto {
    private Long categoryId;
    private String categoryName;
    private Long booksCount;
}