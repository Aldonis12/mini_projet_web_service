package com.projet.MiniProjet.config;

import com.projet.MiniProjet.model.*;
import com.projet.MiniProjet.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               AuthorRepository authorRepository,
                               BookRepository bookRepository,
                               BorrowRepository borrowRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            User admin = new User(null, "Admin", "admin@gmail.com", passwordEncoder.encode("admin123"), Role.ADMIN);
            User user = new User(null, "Test", "user@gmail.com", passwordEncoder.encode("user123"), Role.USER);
            userRepository.save(admin);
            userRepository.save(user);

            Category javaCategory = categoryRepository.save(new Category(null, "Java"));
            Category webCategory = categoryRepository.save(new Category(null, "Web"));
            Category dbCategory = categoryRepository.save(new Category(null, "Base de données"));

            Author author1 = authorRepository.save(new Author("Aldonis", "Auteur 1."));
            Author author2 = authorRepository.save(new Author("Mick", "Auteur 2."));
            Author author3 = authorRepository.save(new Author("Lewis", "Auteur 3."));

            Book book1 = new Book();
            book1.setTitle("Histoire 1");
            book1.setIsbn("123456789");
            book1.setPublicationYear(2018);
            book1.setAvailableQuantity(3);
            book1.setCategory(javaCategory);
            book1.setAuthors(new HashSet<>(Set.of(author1)));
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setTitle("Histoire 2");
            book2.setIsbn("987654321");
            book2.setPublicationYear(2008);
            book2.setAvailableQuantity(2);
            book2.setCategory(webCategory);
            book2.setAuthors(new HashSet<>(Set.of(author3)));
            bookRepository.save(book2);

            Book book3 = new Book();
            book3.setTitle("Histoire 3");
            book3.setIsbn("456789123");
            book3.setPublicationYear(2018);
            book3.setAvailableQuantity(1);
            book3.setCategory(dbCategory);
            book3.setAuthors(new HashSet<>(Set.of(author2)));
            bookRepository.save(book3);

            Borrow borrow = new Borrow();
            borrow.setUser(user);
            borrow.setBook(book1);
            borrow.setBorrowDate(LocalDate.now().minusDays(2));
            borrow.setDueDate(LocalDate.now().plusDays(12));
            borrow.setReturned(false);
            borrowRepository.save(borrow);
        };
    }
}
