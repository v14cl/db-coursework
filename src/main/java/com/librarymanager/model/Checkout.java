package com.librarymanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "checkout")
@SQLDelete(sql = "UPDATE checkout SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkout_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull
    private Client client;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull
    private Book book;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateTaken;

    @NotNull
    @Column(nullable = false)
    private LocalDate deadline;

    private LocalDate dateReturned;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Fine> fines = new ArrayList<>();
}
