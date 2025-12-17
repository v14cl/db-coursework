package com.librarymanager.service;

import com.librarymanager.model.Book;
import com.librarymanager.model.Checkout;
import com.librarymanager.model.Client;
import com.librarymanager.model.Fine;
import com.librarymanager.repository.CheckoutRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.Session;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class CheckoutService {

    private final CheckoutRepository repository;
    private final EntityManager em;

    public CheckoutService(CheckoutRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
                .enableFilter("softDeleteFilter")
                .setParameter("isDeleted", false);
    }

    @Transactional
    public Checkout createCheckout(Long clientId, Long bookId) {

        Client client = em.find(Client.class, clientId);
        if (client == null || client.isDeleted()) {
            throw new IllegalArgumentException("Invalid client");
        }

        Book book = em.find(Book.class, bookId);
        if (book == null || book.isDeleted()) {
            throw new IllegalArgumentException("Invalid book");
        }

        if (!repository.findActiveByBookId(bookId).isEmpty()) {
            throw new IllegalStateException("Book already checked out");
        }

        LocalDate dateTaken = LocalDate.now();
        LocalDate deadline = dateTaken.plusDays(30);

        Checkout checkout = new Checkout();
        checkout.setClient(client);
        checkout.setBook(book);
        checkout.setDateTaken(dateTaken);
        checkout.setDeadline(deadline);

        try {
            return repository.save(checkout);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database constraint violation", e);
        }
    }

    @Transactional
    public Checkout closeCheckout(Long checkoutId) {

        Checkout checkout = repository.findById(checkoutId)
                .orElseThrow(() -> new EntityNotFoundException("Checkout not found"));

        if (checkout.getDateReturned() != null) {
            throw new IllegalStateException("Checkout already closed");
        }

        checkout.setDateReturned(LocalDate.now());

        applyFineIfOverdue(checkout);

        return repository.save(checkout);
    }

    protected void applyFineIfOverdue(Checkout checkout) {

        if (!checkout.getDeadline().isBefore(checkout.getDateReturned())) {
            return;
        }

        long overdueDays = ChronoUnit.DAYS.between(
                checkout.getDeadline(),
                checkout.getDateReturned());

        Fine fine = new Fine();
        fine.setCheckout(checkout);
        fine.setAmount((int) overdueDays * 50);
        fine.setReason("Overdue return");
        fine.setCreatedAt(LocalDateTime.now());
        fine.setPaid(false);

        checkout.getFines().add(fine);
    }

    public List<Checkout> getAll() {
        enableSoftDeleteFilter();
        return em.createQuery("FROM Checkout", Checkout.class).getResultList();
    }

    public Checkout getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Checkout not found"));
    }

    public Checkout update(Long id, Checkout updatedCheckout) {
        Checkout existing = getById(id);
        existing.setClient(updatedCheckout.getClient());
        existing.setBook(updatedCheckout.getBook());
        existing.setDateTaken(updatedCheckout.getDateTaken());
        existing.setDeadline(updatedCheckout.getDeadline());
        existing.setDateReturned(updatedCheckout.getDateReturned());
        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Checkout with id " + id);
        }
    }

    public void softDelete(Long id) {
        Checkout checkout = getById(id);
        checkout.setDeleted(true);
        checkout.setDeletedAt(LocalDateTime.now());
        repository.save(checkout);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }

    public List<Checkout> findActiveByBookId(Long bookId) {
        enableSoftDeleteFilter();
        return repository.findActiveByBookId(bookId);
    }

    public List<Checkout> findOverdue() {
        enableSoftDeleteFilter();
        return repository.findByStatusOverdue();
    }
}
