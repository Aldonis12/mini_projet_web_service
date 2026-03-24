package com.projet.MiniProjet.dto;

import lombok.Data;

@Data
public class BorrowRequestDto {
    private Long userId;
    private Long bookId;
}