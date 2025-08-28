package gr.aueb.cf.expensesApp.repository;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
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

    Optional<Transaction> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);
    Page<Transaction> findByUserIdAndMonthAndYearAndCategoryIdAndIsDeletedFalse(
            Long userId,
            Integer month,
            Long categoryId,
            Integer year,
            Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:userId IS NULL OR t.user.id = :userId) AND " +
            "(:month IS NULL OR t.month = :month) AND " +
            "(:year IS NULL OR t.year = :year) AND " +
            "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
            "(:categoryType IS NULL OR t.category.type = :categoryType) AND " +
            "((:includeDeleted = false AND t.isDeleted = false) OR :includeDeleted = true)")
    Page<Transaction> findTransactionsFiltered(
            @Param("userId")Long userId,
            @Param("month")Integer month,
            @Param("year")Integer year,
            @Param("categoryId")Long categoryId,
            @Param("categoryType")CategoryType categoryType,
            @Param("includeDeleted")boolean includeDeleted,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.isDeleted = true AND t.updatedAt < :cleanDay")
    void deleteSoftDeletedTransactions(@Param("cleanDay") LocalDateTime cleanDay);
}
