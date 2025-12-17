package com.librarymanager.repository;

import com.librarymanager.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    List<Publisher> findByNameContainingIgnoreCase(String name);

    List<Publisher> findByNameStartingWith(String prefix);

    List<Publisher> findByNameEndingWith(String suffix);

    @Query("SELECT p FROM Publisher p WHERE p.isDeleted = false")
    List<Publisher> findAllActive();
}
