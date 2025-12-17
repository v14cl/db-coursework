package com.librarymanager.service;

import com.librarymanager.model.Genre;
import com.librarymanager.repository.GenreRepository;
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
public class GenreService {

    private final GenreRepository repository;
    private final EntityManager em;

    public GenreService(GenreRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
          .enableFilter("softDeleteFilter")
          .setParameter("isDeleted", false);
    }

    public Genre create(Genre genre) {
        return repository.save(genre);
    }

    public List<Genre> getAll() {
        enableSoftDeleteFilter();
        return em.createQuery("FROM Genre", Genre.class).getResultList();
    }

    public Genre getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .filter(g -> !g.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }

    public Genre update(Long id, Genre updatedGenre) {
        Genre existing = getById(id);
        existing.setName(updatedGenre.getName());
        existing.setBooks(updatedGenre.getBooks());
        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Genre with id " + id);
        }
    }

    public void softDelete(Long id) {
        Genre genre = getById(id);
        genre.setDeleted(true);
        genre.setDeletedAt(LocalDateTime.now());
        repository.save(genre);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }
}
