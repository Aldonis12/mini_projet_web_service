package com.projet.MiniProjet.dto;

public class AuthorResponseDto {

    private Long id;
    private String fullName;
    private String biography;

    public AuthorResponseDto() {
    }

    public AuthorResponseDto(Long id, String fullName, String biography) {
        this.id = id;
        this.fullName = fullName;
        this.biography = biography;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
}
