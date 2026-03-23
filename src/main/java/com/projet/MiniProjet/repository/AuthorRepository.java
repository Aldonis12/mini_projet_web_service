package com.projet.MiniProjet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.MiniProjet.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
