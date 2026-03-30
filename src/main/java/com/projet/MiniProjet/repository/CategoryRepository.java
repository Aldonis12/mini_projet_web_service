package com.projet.MiniProjet.repository;

import com.projet.MiniProjet.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT c.name, COUNT(b.id) FROM Category c LEFT JOIN c.books b GROUP BY c.id, c.name")
    List<Object[]> countBooksByCategory();

}