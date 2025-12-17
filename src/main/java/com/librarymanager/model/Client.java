package com.librarymanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "client")
@SQLDelete(sql = "UPDATE client SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@Filter(name = "softDeleteFilter", condition = "is_deleted = :isDeleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private Set<Checkout> checkouts;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
