package org.example.Controller;
import org.example.Entity.Publisher;
import org.example.Service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    // Display all publishers
    @GetMapping
    public String getAllPublishers(Model model) {
        List<Publisher> publishers = publisherService.getAllPublishers();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    // Display form for creating a new publisher
    @GetMapping("/new")
    public String showPublisherForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        return "publisher-form";
    }

    // Handle form submission for creating a new publisher
    @PostMapping("/new")
    public String createPublisher(@ModelAttribute Publisher publisher) {
        publisherService.createPublisher(publisher);
        return "redirect:/publishers";
    }

    // Display form for updating a publisher
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Publisher publisher = publisherService.getPublisherById(id).orElse(null);
        model.addAttribute("publisher", publisher);
        return "publisher-form";
    }

    // Handle form submission for updating a publisher
    @PostMapping("/edit/{id}")
    public String updatePublisher(@PathVariable("id") Long id, @ModelAttribute Publisher updatedPublisher) {
        publisherService.updatePublisher(id, updatedPublisher);
        return "redirect:/publishers";
    }
    // Delete a publisher
    @GetMapping("/delete/{id}")
    public String deletePublisher(@PathVariable("id") Long id) {
        publisherService.deletePublisher(id);
        return "redirect:/publishers";
    }
}
