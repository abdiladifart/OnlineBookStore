package org.example.Service;

import org.example.Entity.Author;
import org.example.Entity.Book;
import org.example.Entity.Publisher;
import org.example.Repository.AuthorRepository;
import org.example.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository; // Add this line

    @Autowired
    private PublisherService publisherService;

    // Retrieve all books
    public List<Book> getAllBooks() {
        return bookRepository.findAllWithAuthors(); // Use the repository method for fetching books with authors
    }

    public List<Book> searchBooks(String searchTerm) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorsNameContainingIgnoreCase(searchTerm, searchTerm);
    }



    // Retrieve a book by ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Create a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Update a book by ID
    public Book updateBook(Long id, Book updatedBook) {
        if (bookRepository.existsById(id)) {
            updatedBook.setId(id);
            return bookRepository.save(updatedBook);
        }
        return null; // or throw an exception indicating the resource was not found
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Add authors to a book
    public void addAuthorsToBook(Long bookId, List<Long> authorIds) {
        if (bookId != null) {
            Book book = bookRepository.findById(bookId).orElse(null);

            if (book != null) {
                List<Author> authors = authorRepository.findAllById(authorIds);
                book.getAuthors().addAll(authors);
                bookRepository.save(book);
            }
        }
    }


    public Book createBookWithGenre(Book book, String genre) {
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    // Save a book with genre
    public Book saveBookWithGenre(Book book, String genre) {
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    // Set publisher for a book
    public void setPublisherForBook(Long bookId, Long publisherId) {
        if (bookId != null && publisherId != null) {
            Book book = bookRepository.findById(bookId).orElse(null);
            Publisher publisher = publisherService.findById(publisherId);

            if (book != null && publisher != null) {
                book.setPublisher(publisher);
                bookRepository.save(book);
            }
        }
    }

    // Retrieve a book by ID
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    // Save a book
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

//    public List<Book> getAllBooks() {
//        return bookRepository.findAllWithAuthors(); // Make sure this method exists in your repository
//    }

    // Add a method to set authors for a book
    public void setAuthorsForBook(Long bookId, List<Long> authorIds) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            Set<Author> authors = authorService.getAuthorsByIds(authorIds);
            book.setAuthors(authors);
            bookRepository.save(book);
        }
    }
    public Page<Book> getBooksOrderedByTitle(Pageable pageable) {
        return bookRepository.findAllByOrderByTitle(pageable);
    }


    public List<Book> getBooksOrderedById() {
        // Use the Spring Data JPA repository method to get books ordered by ID
        return bookRepository.findAllByOrderById();
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }


    public Page<Book> searchBooks(String searchTerm, Pageable pageable) {
        return bookRepository.searchBooks(searchTerm, pageable);
    }

    public long countAllBooks() {
        return bookRepository.count();
    }
}

