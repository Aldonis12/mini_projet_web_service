package com.projet.MiniProjet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projet.MiniProjet.model.Borrow;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByUserId(Long userId);

    @Query("SELECT b.user.id, b.user.fullName, COUNT(b.id) FROM Borrow b GROUP BY b.user.id, b.user.fullName")
    List<Object[]> countBorrowsByUser();
}