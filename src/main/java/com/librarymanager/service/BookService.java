package com.librarymanager.service;

import com.librarymanager.model.Book;
import com.librarymanager.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository repository;
    private final EntityManager em;

    public BookService(BookRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
          .enableFilter("softDeleteFilter")
          .setParameter("isDeleted", false);
    }

    public Book create(Book book) {
        return repository.save(book);
    }

    public List<Book> getAll() {
        enableSoftDeleteFilter();
        return em.createQuery("FROM Book", Book.class).getResultList();
    }

    public Book getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    public Book update(Long id, Book updatedBook) {
        Book existing = getById(id);
        existing.setTitle(updatedBook.getTitle());
        existing.setLanguage(updatedBook.getLanguage());
        existing.setPublisher(updatedBook.getPublisher());
        existing.setAuthors(updatedBook.getAuthors());
        existing.setGenres(updatedBook.getGenres());
        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Book with id " + id);
        }
    }

    public void softDelete(Long id) {
        Book book = getById(id);
        book.setDeleted(true);
        book.setDeletedAt(LocalDateTime.now());
        repository.save(book);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }
}
