package gr.aueb.cf.expensesApp.repository;

import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByCreatedAtBetweenAndCategoryIn
            (LocalDateTime start, LocalDateTime end, List<Category> categories, Pageable pageable);
    Page<Transaction> findByCreatedAtBetween
            (LocalDateTime start, LocalDateTime end, Pageable pageable);
//    Optional<Transaction> findByIdAndIsDeletedFalse(Long id);
    //    List<Transaction> findByAmountGreaterThan(Double amount);
}
