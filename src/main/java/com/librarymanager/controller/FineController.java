package com.librarymanager.controller;

import com.librarymanager.model.Fine;
import com.librarymanager.service.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {

    private final FineService service;

    public FineController(FineService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Fine> create(@RequestBody Fine fine) {
        return ResponseEntity.ok(service.create(fine));
    }

    @GetMapping
    public ResponseEntity<List<Fine>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/checkout/{checkoutId}")
    public ResponseEntity<List<Fine>> getByCheckout(@PathVariable Long checkoutId) {
        return ResponseEntity.ok(service.getByCheckoutId(checkoutId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fine> update(@PathVariable Long id, @RequestBody Fine fine) {
        return ResponseEntity.ok(service.update(id, fine));
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
