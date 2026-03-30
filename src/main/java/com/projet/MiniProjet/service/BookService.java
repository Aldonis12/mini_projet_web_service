package com.projet.MiniProjet.service;

import com.projet.MiniProjet.dto.BookRequestDto;
import com.projet.MiniProjet.dto.BookResponseDto;
import com.projet.MiniProjet.exception.ResourceNotFoundException;
import com.projet.MiniProjet.model.Author;
import com.projet.MiniProjet.model.Book;
import com.projet.MiniProjet.model.Category;
import com.projet.MiniProjet.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, CategoryService categoryService, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

    public List<BookResponseDto> findAll() {
        return bookRepository.findAll().stream().map(this::toDto).toList();
    }

    public BookResponseDto findById(Long id) {
        return toDto(getEntityById(id));
    }

    public BookResponseDto create(BookRequestDto dto) {
        Book book = new Book();
        fillBookFromDto(book, dto);
        return toDto(bookRepository.save(book));
    }

    public BookResponseDto update(Long id, BookRequestDto dto) {
        Book book = getEntityById(id);
        fillBookFromDto(book, dto);
        return toDto(bookRepository.save(book));
    }

    public void delete(Long id) {
        bookRepository.delete(getEntityById(id));
    }

    public Book getEntityById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
    }

    private void fillBookFromDto(Book book, BookRequestDto dto) {
        Category category = categoryService.getEntityById(dto.getCategoryId());
        Set<Author> authors = new HashSet<>();

        if (dto.getAuthorIds() != null) {
            for (Long authorId : dto.getAuthorIds()) {
                authors.add(authorService.getEntityById(authorId));
            }
        }

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAvailableQuantity(dto.getAvailableQuantity() == null ? 0 : dto.getAvailableQuantity());
        book.setCategory(category);
        book.setAuthors(authors);
    }

    private BookResponseDto toDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setAvailableQuantity(book.getAvailableQuantity());
        dto.setCategoryId(book.getCategory() != null ? book.getCategory().getId() : null);
        dto.setCategoryName(book.getCategory() != null ? book.getCategory().getName() : null);
        dto.setAuthors(book.getAuthors().stream().map(Author::getFullName).sorted().toList());
        return dto;
    }
}
