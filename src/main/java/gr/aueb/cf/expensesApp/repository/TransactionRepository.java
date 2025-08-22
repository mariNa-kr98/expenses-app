package gr.aueb.cf.expensesApp.repository;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUserIdAndCreatedAtBetweenAndCategoryIdInAndIsDeletedFalse
            (Long userId,
             LocalDateTime start,
             LocalDateTime end,
             List<Long> categoryIds,
             Pageable pageable);
    Page<Transaction> findByUserIdAndCreatedAtBetweenAndIsDeletedFalse
            (Long userId,
             LocalDateTime start,
             LocalDateTime end,
             Pageable pageable);
    Page<Transaction> findByUserIdAndCreatedAtBetweenAndCategory_TypeAndIsDeletedFalse(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            CategoryType categoryType,
            Pageable pageable
    );
    Page<Transaction> findByUserIdAndCreatedAtBetweenAndCategoryIdAndIsDeletedFalse(
            Long userId,
            LocalDateTime start,
            LocalDateTime end,
            Long categoryId,
            Pageable pageable
    );
    Optional<Transaction> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.isDeleted = true AND t.updatedAt < :cleanDay")
    void deleteSoftDeletedTransactions(@Param("cleanDay") LocalDateTime cleanDay);
}
