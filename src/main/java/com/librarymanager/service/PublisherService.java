package com.librarymanager.service;

import com.librarymanager.model.Publisher;
import com.librarymanager.repository.PublisherRepository;
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
public class PublisherService {

    private final PublisherRepository repository;
    private final EntityManager em;

    public PublisherService(PublisherRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
          .enableFilter("softDeleteFilter")
          .setParameter("isDeleted", false);
    }

    public Publisher create(Publisher publisher) {
        return repository.save(publisher);
    }

    public List<Publisher> getAll() {
        enableSoftDeleteFilter();
        return em.createQuery("FROM Publisher", Publisher.class).getResultList();
    }

    public Publisher getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));
    }

    public Publisher update(Long id, Publisher updatedPublisher) {
        Publisher existing = getById(id);
        existing.setName(updatedPublisher.getName());
        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Publisher with id " + id);
        }
    }

    public void softDelete(Long id) {
        Publisher publisher = getById(id);
        publisher.setDeleted(true);
        publisher.setDeletedAt(LocalDateTime.now());
        repository.save(publisher);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }
}
