package com.projet.MiniProjet.dto;

/**
 * Petit DTO pratique pour retourner un message simple.
 */
public class ApiMessageDto {

    private String message;

    public ApiMessageDto() {
    }

    public ApiMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
