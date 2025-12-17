package com.librarymanager.service;

import com.librarymanager.model.Author;
import com.librarymanager.repository.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository repository;
    private final EntityManager em;

    public AuthorService(AuthorRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
          .enableFilter("softDeleteFilter")
          .setParameter("isDeleted", false);
    }

    public Author create(Author author) {
        return repository.save(author);
    }

    public List<Author> getAll() {
        enableSoftDeleteFilter();
        return em.createQuery("FROM Author", Author.class).getResultList();
    }

    public Author getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    public Author update(Long id, Author updatedAuthor) {
        Author existing = getById(id);
        existing.setFirstName(updatedAuthor.getFirstName());
        existing.setLastName(updatedAuthor.getLastName());
        existing.setBooks(updatedAuthor.getBooks());
        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Author with id " + id);
        }
    }

    public void softDelete(Long id) {
        Author author = getById(id);
        author.setDeleted(true);
        author.setDeletedAt(LocalDateTime.now());
        repository.save(author);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }
}
