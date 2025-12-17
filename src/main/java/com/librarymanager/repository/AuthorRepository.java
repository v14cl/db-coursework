package com.librarymanager.repository;

import com.librarymanager.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByFirstName(String firstName);

    List<Author> findByLastName(String lastName);

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT a FROM Author a WHERE a.isDeleted = false")
    List<Author> findAllActive();
}
