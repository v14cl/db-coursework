package com.librarymanager.repository;

import com.librarymanager.model.Book;
import com.librarymanager.model.Language;
import com.librarymanager.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByPublisher(Publisher publisher);

    List<Book> findByLanguage(Language language);

    @Query("SELECT b FROM Book b WHERE b.isDeleted = false")
    List<Book> findAllActive();
}
