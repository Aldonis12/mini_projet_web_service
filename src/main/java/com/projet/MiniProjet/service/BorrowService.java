package com.projet.MiniProjet.service;

import com.projet.MiniProjet.dto.BorrowRequestDto;
import com.projet.MiniProjet.dto.BorrowResponseDto;
import com.projet.MiniProjet.dto.UserBorrowSummaryDto;
import com.projet.MiniProjet.exception.BadRequestException;
import com.projet.MiniProjet.exception.ResourceNotFoundException;
import com.projet.MiniProjet.model.Book;
import com.projet.MiniProjet.model.Borrow;
import com.projet.MiniProjet.model.User;
import com.projet.MiniProjet.repository.BookRepository;
import com.projet.MiniProjet.repository.BorrowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserService userService;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRepository borrowRepository, UserService userService, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userService = userService;
        this.bookRepository = bookRepository;
    }

    public List<BorrowResponseDto> findAll() {
        return borrowRepository.findAll().stream().map(this::toDto).toList();
    }

    public BorrowResponseDto findById(Long id) {
        return toDto(getEntityById(id));
    }

    public BorrowResponseDto create(BorrowRequestDto dto) {
        User user = userService.getEntityById(dto.getUserId());
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + dto.getBookId()));

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("Indisponible");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(14));
        borrow.setReturned(false);

        return toDto(borrowRepository.save(borrow));
    }

    public BorrowResponseDto returnBook(Long borrowId) {
        Borrow borrow = getEntityById(borrowId);
        if (borrow.isReturned()) {
            throw new BadRequestException("Cet emprunt est déjà retourné.");
        }

        borrow.setReturned(true);
        borrow.setReturnDate(LocalDate.now());

        Book book = borrow.getBook();
        int currentQuantity = book.getAvailableQuantity() == null ? 0 : book.getAvailableQuantity();
        book.setAvailableQuantity(currentQuantity + 1);
        bookRepository.save(book);

        return toDto(borrowRepository.save(borrow));
    }

    public void delete(Long id) {
        borrowRepository.delete(getEntityById(id));
    }

    public List<UserBorrowSummaryDto> countBorrowsByUser() {
        return borrowRepository.countBorrowsByUser().stream()
                .map(row -> new UserBorrowSummaryDto((Long) row[0], (String) row[1], (Long) row[2]))
                .toList();
    }

    public Borrow getEntityById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'id : " + id));
    }

    private BorrowResponseDto toDto(Borrow borrow) {
        BorrowResponseDto dto = new BorrowResponseDto();
        dto.setId(borrow.getId());
        dto.setUserId(borrow.getUser().getId());
        dto.setUserName(borrow.getUser().getFullName());
        dto.setBookId(borrow.getBook().getId());
        dto.setBookTitle(borrow.getBook().getTitle());
        dto.setBorrowDate(borrow.getBorrowDate());
        dto.setDueDate(borrow.getDueDate());
        dto.setReturnDate(borrow.getReturnDate());
        dto.setReturned(borrow.isReturned());
        return dto;
    }
}
