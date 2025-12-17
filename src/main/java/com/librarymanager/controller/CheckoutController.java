package com.librarymanager.controller;

import com.librarymanager.model.Checkout;
import com.librarymanager.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkouts")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping
public ResponseEntity<Checkout> create(@RequestParam Long clientId,
                                       @RequestParam Long bookId) {
    Checkout created = service.createCheckout(clientId, bookId);
    return ResponseEntity.ok(created);
}
    @PostMapping("/{id}/close")
    public ResponseEntity<Checkout> closeCheckout(@PathVariable Long id) {
        return ResponseEntity.ok(service.closeCheckout(id));
    }

    @GetMapping
    public ResponseEntity<List<Checkout>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Checkout> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Checkout> update(@PathVariable Long id, @RequestBody Checkout checkout) {
        return ResponseEntity.ok(service.update(id, checkout));
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

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Checkout>> findActiveByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(service.findActiveByBookId(bookId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Checkout>> findOverdue() {
        return ResponseEntity.ok(service.findOverdue());
    }
}
