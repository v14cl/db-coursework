package com.librarymanager.controller;

import com.librarymanager.model.Author;
import com.librarymanager.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        return ResponseEntity.ok(service.create(author));
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Long id, @RequestBody Author author) {
        return ResponseEntity.ok(service.update(id, author));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        service.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}
