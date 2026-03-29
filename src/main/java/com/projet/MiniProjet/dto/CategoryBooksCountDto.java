package com.projet.MiniProjet.dto;

public class CategoryBooksCountDto {

    private String categoryName;
    private Long booksCount;

    public CategoryBooksCountDto() {
    }

    public CategoryBooksCountDto(String categoryName, Long booksCount) {
        this.categoryName = categoryName;
        this.booksCount = booksCount;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Long getBooksCount() { return booksCount; }
    public void setBooksCount(Long booksCount) { this.booksCount = booksCount; }
}
