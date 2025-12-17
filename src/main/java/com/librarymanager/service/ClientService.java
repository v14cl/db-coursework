package com.librarymanager.service;

import com.librarymanager.model.Client;
import com.librarymanager.repository.ClientRepository;
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
public class ClientService {

    private final ClientRepository repository;
    private final EntityManager em;

    public ClientService(ClientRepository repository, EntityManager em) {
        this.repository = repository;
        this.em = em;
    }

    private void enableSoftDeleteFilter() {
        em.unwrap(Session.class)
          .enableFilter("softDeleteFilter")
          .setParameter("isDeleted", false);
    }

    public Client create(Client client) {
        return repository.save(client);
    }

    public List<Client> getAll() {
        enableSoftDeleteFilter();
        return em.createQuery("FROM Client", Client.class).getResultList();
    }

    public Client getById(Long id) {
        enableSoftDeleteFilter();
        return repository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }

    public Client update(Long id, Client updatedClient) {
        Client existing = getById(id);
        existing.setFirstName(updatedClient.getFirstName());
        existing.setLastName(updatedClient.getLastName());
        existing.setPhoneNumber(updatedClient.getPhoneNumber());
        try {
            return repository.save(existing);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockException("Conflict updating Client with id " + id);
        }
    }

    public void softDelete(Long id) {
        Client client = getById(id);
        client.setDeleted(true);
        client.setDeletedAt(LocalDateTime.now());
        repository.save(client);
    }

    public void hardDelete(Long id) {
        repository.deleteById(id);
    }
}
