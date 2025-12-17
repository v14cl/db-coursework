package com.librarymanager.repository;

import com.librarymanager.model.Checkout;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    List<Checkout> findByClientId(Long clientId);

    @Query("SELECT c FROM Checkout c WHERE c.isDeleted = false")
    List<Checkout> findAllActive();

    @Query("""
            SELECT c FROM Checkout c
            WHERE c.dateReturned IS NULL
              AND c.deadline >= CURRENT_DATE
              AND c.isDeleted = false
            """)
    List<Checkout> findByStatusActive();

    @Query("""
            SELECT c FROM Checkout c
            WHERE c.dateReturned IS NULL
              AND c.deadline < CURRENT_DATE
              AND c.isDeleted = false
            """)
    List<Checkout> findByStatusOverdue();

    @Query("""
            SELECT c FROM Checkout c
            WHERE c.dateReturned IS NOT NULL
              AND c.isDeleted = false
            """)
    List<Checkout> findByStatusReturned();

    @Query("""
            SELECT c FROM Checkout c
            WHERE c.book.id = :bookId
              AND c.dateReturned IS NULL
              AND c.isDeleted = false
            """)
    List<Checkout> findActiveByBookId(Long bookId);

    @Query("""
            SELECT c FROM Checkout c
            JOIN c.book b
            JOIN c.client cl
            WHERE c.dateReturned IS NULL
              AND c.isDeleted = false
            ORDER BY c.deadline ASC
            """)
    List<Checkout> findActiveCheckoutsWithBookAndClient(Pageable pageable);

    @Query(value = """
                SELECT
                    cl.client_id,
                    cl.first_name || ' ' || cl.last_name AS client_name,
                    COUNT(c.checkout_id) AS total_checkouts
                FROM client cl
                JOIN checkout c ON c.client_id = cl.client_id
                GROUP BY cl.client_id, cl.first_name, cl.last_name
                ORDER BY total_checkouts DESC
            """, nativeQuery = true)
    List<Object[]> getClientsWithCheckouts();

}
