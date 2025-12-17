package com.librarymanager.service;

import com.librarymanager.model.Fine;
import com.librarymanager.repository.FineRepository;
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
public class FineService {

    private final FineRepository repository;
    private final EntityManager em;

    public FineService(FineRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
          .enableFilter("softDeleteFilter")
          .setParameter("isDeleted", false);
    }

    public Fine create(Fine fine) {
        return repository.save(fine);
    }

    public List<Fine> getAll() {
        enableSoftDeleteFilter();
        return repository.findAllActive();
    }

    public Fine getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .filter(f -> !f.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Fine not found"));
    }

    public List<Fine> getByCheckoutId(Long checkoutId) {
        enableSoftDeleteFilter();
        return repository.findByCheckoutId(checkoutId);
    }

    public Fine update(Long id, Fine updatedFine) {
        Fine existing = getById(id);
        existing.setAmount(updatedFine.getAmount());
        existing.setReason(updatedFine.getReason());

        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Fine with id " + id);
        }
    }

    public void softDelete(Long id) {
        Fine fine = getById(id);
        fine.setDeleted(true);
        fine.setDeletedAt(LocalDateTime.now());
        repository.save(fine);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }
}
