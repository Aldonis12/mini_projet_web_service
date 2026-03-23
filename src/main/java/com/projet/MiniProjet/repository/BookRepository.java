package com.projet.MiniProjet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.MiniProjet.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
