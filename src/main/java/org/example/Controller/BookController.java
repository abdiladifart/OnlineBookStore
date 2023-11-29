package org.example.Controller;

import org.example.Entity.Author;
import org.example.Entity.Book;
import org.example.Entity.Publisher;
import org.example.Repository.AuthorRepository;
import org.example.Repository.BookRepository;
import org.example.Repository.PublisherRepository;
import org.example.Service.AuthorService;
import org.example.Service.BookService;
import org.example.Service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    // Display all books
//    @GetMapping
//    public String getAllBooks(Model model, @RequestParam(name = "search", required = false) String search, @RequestParam(defaultValue = "0") int page) {
//        int pageSize = 10;  // Set your desired page size
//
//        Pageable pageable = PageRequest.of(page, pageSize);
//        Page<Book> books;
//
//        if (search != null && !search.isEmpty()) {
//            books = bookService.searchBooks(search, pageable);
//        } else {
//            books = bookService.getAllBooks(pageable);
//        }
//
//        model.addAttribute("books", books.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", books.getTotalPages());
//
//        return "books";
//    }
    @GetMapping
    public String getAllBooks(Model model, @RequestParam(name = "search", required = false) String search, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 2;  // Set your desired page size

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Book> books;

        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search, pageable);
        } else {
            books = bookService.getAllBooks(pageable);
        }

        model.addAttribute("books", books);  // Ensure "books" is of type Page<Book>
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());

        // Add the total count of books to the model
        long totalCount = bookService.countAllBooks();
        model.addAttribute("totalCount", totalCount);

        return "books";
    }







    // Display form for creating a new book
    @GetMapping("/new")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "book-form";
    }

    // Handle form submission for creating a new book
    @PostMapping("/new")
    public String createBook(@ModelAttribute Book book, @RequestParam("authorIds") List<Long> authorIds, HttpServletRequest request) {
        Long publisherId = book.getPublisher().getId();  // Get the selected publisher ID from the book object
        bookService.addAuthorsToBook(book.getId(), authorIds);
        bookService.setPublisherForBook(book.getId(), publisherId);
        bookService.createBook(book);
        bookService.setAuthorsForBook(book.getId(), authorIds);


        String priceString = request.getParameter("price");
        if (priceString != null && !priceString.isEmpty()) {
            BigDecimal price = new BigDecimal(priceString);
            book.setPrice(price);
        }

        String publicationYearString = request.getParameter("publicationYear");
        if (publicationYearString != null && !publicationYearString.isEmpty()) {
            Integer publicationYear = Integer.parseInt(publicationYearString);
            book.setPublicationYear(publicationYear);
        }

        System.out.println("Publisher ID: " + publisherId);
        return "redirect:/books";
    }



    // Display form for updating a book
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id).orElse(null);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "book-form";
    }

    // Handle form submission for updating a book
    @PostMapping("/edit/{id}")
    public String updateBookWithGenre(@PathVariable("id") Long id, @ModelAttribute Book updatedBook, @RequestParam("authorIds") List<Long> authorIds, @RequestParam("genre") String genre, HttpServletRequest request) {
        Long publisherId = Long.parseLong(request.getParameter("publisherId"));
        bookService.addAuthorsToBook(updatedBook.getId(), authorIds);

        // Set the publisher for the updated book
        Publisher publisher = publisherService.findById(publisherId);
        updatedBook.setPublisher(publisher);

        // Save the updated book with genre
        bookService.saveBookWithGenre(updatedBook, genre);  // Updated line

        return "redirect:/books";
    }

    // Delete a book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book, @RequestParam("authorIds") List<Long> authorIds) {
        // Set the authors based on the selected author IDs
        List<Author> selectedAuthors = authorRepository.findAllById(authorIds);
        book.setAuthors(new HashSet<>(selectedAuthors));

        // Save the book
        bookRepository.save(book);

        // Redirect to the book list page or any other appropriate page
        return "redirect:/books";
    }
    @GetMapping("/order")
    public String orderBooks(Model model, @RequestParam(name = "order", required = false) String order, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10); // Assuming a page size of 10, you can adjust it accordingly

        Page<Book> orderedBooks;

        // Get the ordered list of books based on the selected order
        if ("title".equals(order)) {
            orderedBooks = bookService.getBooksOrderedByTitle(pageable);
        } else {
            // Default order by ID
            orderedBooks = bookService.getAllBooks(pageable);
        }

        // Add the ordered books to the model
        model.addAttribute("books", orderedBooks);

        // Add the order value to the model for displaying in the template
        model.addAttribute("currentOrder", order);

        return "books";
    }


    @GetMapping("/books")
    public Page<Book> searchBooks(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return bookService.searchBooks(searchTerm, pageable);
    }

    public long countSearchBooks(String searchTerm) {
        return bookRepository.countSearchBooks(searchTerm);
    }

}
