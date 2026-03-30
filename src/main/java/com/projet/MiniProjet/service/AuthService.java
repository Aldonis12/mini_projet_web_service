package com.projet.MiniProjet.service;

import com.projet.MiniProjet.dto.LoginRequestDto;
import com.projet.MiniProjet.dto.LoginResponseDto;
import com.projet.MiniProjet.dto.RegisterRequestDto;
import com.projet.MiniProjet.dto.UserResponseDto;
import com.projet.MiniProjet.exception.BadRequestException;
import com.projet.MiniProjet.model.Role;
import com.projet.MiniProjet.model.User;
import com.projet.MiniProjet.repository.UserRepository;
import com.projet.MiniProjet.security.CustomUserDetailsService;
import com.projet.MiniProjet.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public UserResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Cet email existe déjà.");
        }

        Role role = Role.USER;
        if (dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN")) {
            role = Role.ADMIN;
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        user = userRepository.save(user);
        return new UserResponseDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Utilisateur introuvable."));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDto(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
