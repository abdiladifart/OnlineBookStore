package org.example.Service;

import org.example.Entity.Publisher;
import org.example.Repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    // Retrieve all publishers
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    // Retrieve a publisher by ID
    public Optional<Publisher> getPublisherById(Long id) {
        return publisherRepository.findById(id);
    }

    // Create a new publisher
    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    // Update a publisher by ID
    public Publisher updatePublisher(Long id, Publisher updatedPublisher) {
        if (publisherRepository.existsById(id)) {
            updatedPublisher.setId(id);
            return publisherRepository.save(updatedPublisher);
        }
        return null;
    }

    // Delete a publisher by ID
    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }

    public Publisher findById(Long id) {
        return publisherRepository.findById(id).orElse(null);
    }
}
