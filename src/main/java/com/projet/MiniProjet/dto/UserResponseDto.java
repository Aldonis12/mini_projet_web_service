package com.projet.MiniProjet.dto;

import com.projet.MiniProjet.model.Role;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}