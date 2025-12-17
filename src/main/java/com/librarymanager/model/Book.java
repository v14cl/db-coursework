package com.librarymanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "book")
@SQLDelete(sql = "UPDATE book SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    @NotNull
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;

    @ManyToMany
    @JoinTable(
            name = "genre_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;
}
