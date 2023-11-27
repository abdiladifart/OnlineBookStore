package org.example.Repository;

import org.example.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Add a query method to fetch books with authors
    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllWithAuthors();

    List<Book> findByTitleContainingIgnoreCaseOrAuthorsNameContainingIgnoreCase(String searchTerm, String searchTerm1);
    List<Book> findAllByOrderByTitle();
    List<Book> findAllByOrderById();


}
