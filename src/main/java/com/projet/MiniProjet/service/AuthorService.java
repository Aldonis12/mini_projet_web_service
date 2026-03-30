package com.projet.MiniProjet.service;

import com.projet.MiniProjet.dto.AuthorRequestDto;
import com.projet.MiniProjet.dto.AuthorResponseDto;
import com.projet.MiniProjet.exception.ResourceNotFoundException;
import com.projet.MiniProjet.model.Author;
import com.projet.MiniProjet.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll().stream().map(this::toDto).toList();
    }

    public AuthorResponseDto findById(Long id) {
        return toDto(getEntityById(id));
    }

    public AuthorResponseDto create(AuthorRequestDto dto) {
        Author author = new Author();
        author.setFullName(dto.getFullName());
        author.setBiography(dto.getBiography());
        return toDto(authorRepository.save(author));
    }

    public AuthorResponseDto update(Long id, AuthorRequestDto dto) {
        Author author = getEntityById(id);
        author.setFullName(dto.getFullName());
        author.setBiography(dto.getBiography());
        return toDto(authorRepository.save(author));
    }

    public void delete(Long id) {
        authorRepository.delete(getEntityById(id));
    }

    public Author getEntityById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur non trouvé avec l'id : " + id));
    }

    private AuthorResponseDto toDto(Author author) {
        return new AuthorResponseDto(author.getId(), author.getFullName(), author.getBiography());
    }
}
