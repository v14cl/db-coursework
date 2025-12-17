package com.librarymanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "publisher")
@SQLDelete(sql = "UPDATE publisher SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "publisher")
    @ToString.Exclude
    private Set<Book> books;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;
}
