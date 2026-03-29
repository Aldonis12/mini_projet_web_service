package com.projet.MiniProjet.dto;

public class UserBorrowSummaryDto {

    private Long userId;
    private String fullName;
    private Long borrowsCount;

    public UserBorrowSummaryDto() {
    }

    public UserBorrowSummaryDto(Long userId, String fullName, Long borrowsCount) {
        this.userId = userId;
        this.fullName = fullName;
        this.borrowsCount = borrowsCount;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Long getBorrowsCount() { return borrowsCount; }
    public void setBorrowsCount(Long borrowsCount) { this.borrowsCount = borrowsCount; }
}
