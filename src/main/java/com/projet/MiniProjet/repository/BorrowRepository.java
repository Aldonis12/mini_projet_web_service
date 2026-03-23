package com.projet.MiniProjet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.MiniProjet.model.Borrow;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByUserId(Long userId);
}