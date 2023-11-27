package org.example.Controller;

import org.example.Entity.Author;
import org.example.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // Display all authors
    @GetMapping
    public String getAllAuthors(Model model) {
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);
        return "authors";
    }

    // Display form for creating a new author
    @GetMapping("/new")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author-form";
    }

    // Handle form submission for creating a new author
    @PostMapping("/new")
    public String createAuthor(@ModelAttribute Author author) {
        authorService.createAuthor(author);
        return "redirect:/authors";
    }

    // Display form for updating an author
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Author author = authorService.getAuthorById(id).orElse(null);
        model.addAttribute("author", author);
        return "author-form";
    }

    // Handle form submission for updating an author
    @PostMapping("/edit/{id}")
    public String updateAuthor(@PathVariable("id") Long id, @ModelAttribute Author updatedAuthor) {
        authorService.updateAuthor(id, updatedAuthor);
        return "redirect:/authors";
    }

    // Delete an author
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}
