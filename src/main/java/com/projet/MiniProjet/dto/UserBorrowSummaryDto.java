package com.projet.MiniProjet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBorrowSummaryDto {
    private Long userId;
    private String userName;
    private Long totalBorrows;
    private Long returnedBorrows;
    private Long activeBorrows;
}