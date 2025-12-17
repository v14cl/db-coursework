package com.librarymanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "author")
@SQLDelete(sql = "UPDATE author SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false)
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    @ToString.Exclude
    private Set<Book> books;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;
}
