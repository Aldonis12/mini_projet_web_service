package com.projet.MiniProjet.service;

import com.projet.MiniProjet.dto.UserResponseDto;
import com.projet.MiniProjet.exception.ResourceNotFoundException;
import com.projet.MiniProjet.model.User;
import com.projet.MiniProjet.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserResponseDto findById(Long id) {
        return toDto(getEntityById(id));
    }

    public void delete(Long id) {
        User user = getEntityById(id);
        userRepository.delete(user);
    }

    public User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
