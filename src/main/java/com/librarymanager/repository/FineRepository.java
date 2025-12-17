package com.librarymanager.repository;

import com.librarymanager.model.Fine;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

  @Query("SELECT f FROM Fine f WHERE f.isDeleted = false")
  List<Fine> findAllActive();

  @Query("""
      SELECT f FROM Fine f
      WHERE f.checkout.id = :checkoutId
        AND f.isDeleted = false
      """)
  List<Fine> findByCheckoutId(Long checkoutId);

  @Query("""
      SELECT f FROM Fine f
      JOIN f.checkout c
      JOIN c.client cl
      JOIN c.book b
      WHERE f.isDeleted = false
        AND f.isPaid = false
      ORDER BY f.createdAt DESC
      """)
  List<Fine> findUnpaidFinesWithCheckoutBookAndClient(Pageable pageable);

}
