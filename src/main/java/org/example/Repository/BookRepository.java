package org.example.Repository;

import org.example.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
//    List<Book> findAllWithAuthors();
}
