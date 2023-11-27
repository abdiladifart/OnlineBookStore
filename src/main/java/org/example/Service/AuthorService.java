package org.example.Service;

import org.example.Entity.Author;
import org.example.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    // Retrieve all authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Retrieve an author by ID
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    // Create a new author
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Update an author by ID
    public Author updateAuthor(Long id, Author updatedAuthor) {
        if (authorRepository.existsById(id)) {
            updatedAuthor.setId(id);
            return authorRepository.save(updatedAuthor);
        }
        return null; // or throw an exception indicating the resource was not found
    }


    // Retrieve authors by a list of IDs
    public Set<Author> getAuthorsByIds(List<Long> authorIds) {
        return authorIds.stream()
                .map(authorRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    // Delete an author by ID
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    // Add a method to retrieve all authors with associated books
    public List<Author> getAllAuthorsWithBooks() {
        return authorRepository.findAllWithBooks();
    }

}
