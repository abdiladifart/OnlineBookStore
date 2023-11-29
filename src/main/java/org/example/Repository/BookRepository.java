package org.example.Repository;

import org.example.Entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Add a query method to fetch books with authors
    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllWithAuthors();

    List<Book> findByTitleContainingIgnoreCaseOrAuthorsNameContainingIgnoreCase(String searchTerm, String searchTerm1);
    Page<Book> findAllByOrderByTitle(Pageable pageable);
    List<Book> findAllByOrderById();

    Page<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Book> searchBooks(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Book b JOIN b.authors a WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    long countBooks(@Param("searchTerm") String searchTerm);

    @Query("SELECT COUNT(b) FROM Book b JOIN b.authors a WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    long countSearchBooks(@Param("searchTerm") String searchTerm);

}
