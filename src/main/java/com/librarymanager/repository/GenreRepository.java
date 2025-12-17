package com.librarymanager.repository;

import com.librarymanager.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByNameContainingIgnoreCase(String name);

    List<Genre> findByNameStartingWith(String prefix);

    List<Genre> findByNameEndingWith(String suffix);

    @Query("SELECT g FROM Genre g WHERE g.isDeleted = false")
    List<Genre> findAllActive();
}
