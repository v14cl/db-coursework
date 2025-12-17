package com.librarymanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "genre")
@SQLDelete(sql = "UPDATE genre SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    @ToString.Exclude
    private Set<Book> books;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;
}
