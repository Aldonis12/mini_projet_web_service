package com.projet.MiniProjet.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BorrowRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    private LocalDate dueDate;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
