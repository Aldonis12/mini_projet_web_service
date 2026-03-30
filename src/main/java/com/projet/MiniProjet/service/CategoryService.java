package com.projet.MiniProjet.service;

import com.projet.MiniProjet.dto.CategoryBooksCountDto;
import com.projet.MiniProjet.dto.CategoryRequestDto;
import com.projet.MiniProjet.dto.CategoryResponseDto;
import com.projet.MiniProjet.exception.BadRequestException;
import com.projet.MiniProjet.exception.ResourceNotFoundException;
import com.projet.MiniProjet.model.Category;
import com.projet.MiniProjet.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream().map(this::toDto).toList();
    }

    public CategoryResponseDto findById(Long id) {
        return toDto(getEntityById(id));
    }

    public CategoryResponseDto create(CategoryRequestDto dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("Cette catégorie existe déjà.");
        }
        Category category = new Category();
        category.setName(dto.getName());
        return toDto(categoryRepository.save(category));
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
        Category category = getEntityById(id);
        category.setName(dto.getName());
        return toDto(categoryRepository.save(category));
    }

    public void delete(Long id) {
        categoryRepository.delete(getEntityById(id));
    }

    public List<CategoryBooksCountDto> countBooksByCategory() {
        return categoryRepository.countBooksByCategory().stream()
                .map(row -> new CategoryBooksCountDto((String) row[0], (Long) row[1]))
                .toList();
    }

    public Category getEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id : " + id));
    }

    private CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}
