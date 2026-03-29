package com.projet.MiniProjet.controller;

import com.projet.MiniProjet.dto.LoginRequestDto;
import com.projet.MiniProjet.dto.LoginResponseDto;
import com.projet.MiniProjet.dto.RegisterRequestDto;
import com.projet.MiniProjet.dto.UserResponseDto;
import com.projet.MiniProjet.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints d'authentification.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Créer un compte",
            requestBody = @RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"fullName\": \"Nouveau User\",\n" +
                                            "  \"email\": \"newuser@library.com\",\n" +
                                            "  \"password\": \"password123\",\n" +
                                            "  \"role\": \"USER\"\n" +
                                            "}"
                            )
                    )
            )
    )
    public ResponseEntity<UserResponseDto> register(
            @Valid @org.springframework.web.bind.annotation.RequestBody RegisterRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Se connecter",
            requestBody = @RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"email\": \"admin@library.com\",\n" +
                                            "  \"password\": \"admin123\"\n" +
                                            "}"
                            )
                    )
            )
    )
    public ResponseEntity<LoginResponseDto> login(
            @Valid @org.springframework.web.bind.annotation.RequestBody LoginRequestDto dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
