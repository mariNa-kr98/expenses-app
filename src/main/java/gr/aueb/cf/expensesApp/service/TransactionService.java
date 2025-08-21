package gr.aueb.cf.expensesApp.service;


import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.core.enums.ErrorCode;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.dto.TransactionInsertDTO;
import gr.aueb.cf.expensesApp.dto.TransactionReadOnlyDTO;
import gr.aueb.cf.expensesApp.dto.TransactionUpdateDTO;
import gr.aueb.cf.expensesApp.mapper.Mapper;
import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.model.Transaction;
import gr.aueb.cf.expensesApp.model.User;
import gr.aueb.cf.expensesApp.repository.CategoryRepository;
import gr.aueb.cf.expensesApp.repository.TransactionRepository;
import gr.aueb.cf.expensesApp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private  final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public TransactionReadOnlyDTO saveTransaction(TransactionInsertDTO transactionInsertDTO) {

        Category category = categoryRepository.findById(transactionInsertDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Transaction transaction = mapper.mapToTransactionEntity(transactionInsertDTO);
        transaction.setAmount(transactionInsertDTO.getAmount());
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setIsDeleted(false);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapper.mapToTransactionReadOnlyDTO(savedTransaction);

    }

    @Transactional
    public Optional<Transaction> modifyTransaction
            (Long transactionId, Long userId,TransactionUpdateDTO dto){

        Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(transactionId, userId);

        if (optionalTransaction.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Transaction not found.");
        }

        return optionalTransaction.map(transaction ->{
                    if (dto.getAmount() != null) {
                        transaction.setAmount(dto.getAmount());
                    }
                    if(dto.getCategoryId() != null){
                        Category category = categoryRepository.findById(dto.getCategoryId())
                                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Category not found."));
                        transaction.setCategory(category);
                    }
                    if (dto.getIsDeleted() !=null) {
                        transaction.setIsDeleted(dto.getIsDeleted());
                    }
                    if (dto.getNotes() != null) {
                        transaction.setNotes(dto.getNotes());
                    }
                    transaction.setUpdatedAt(LocalDateTime.now());
                    return transactionRepository.save(transaction);
        });
    }

    @Transactional
    public String deleteTransaction(Long transactionId, Long userId){

        Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(transactionId, userId);

        if (optionalTransaction.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Transaction not found.");
        }

        Transaction transaction = optionalTransaction.get();
        transaction.setIsDeleted(true);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
//        transactionRepository.deleteById(transactionId);
        return "Transaction with id " + transactionId + " was soft-deleted succesfully.";
    }

    @Transactional
    public Page<TransactionReadOnlyDTO> getPaginatedTransactions
            (Long userId,
             int page,
             int size,
             int month,
             int year,
             List<Long> categoryIds,
             @Nullable Long categoryId,
             @Nullable CategoryType categoryType) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = startDate.atStartOfDay();

        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(LocalTime.MAX);

        Page<Transaction> transactionsPage;

        if (categoryId != null) {
            transactionsPage = transactionRepository
                    .findByUserIdAndCreatedAtBetweenAndCategoryIdAndIsDeletedFalse(userId, startOfMonth, endOfMonth, categoryId, pageable);
        } else if (categoryType != null) {
            transactionsPage = transactionRepository
                    .findByCreatedAtBetweenAndCategory_TypeAndIsDeletedFalse(userId, startOfMonth, endOfMonth, categoryType, pageable);
        }else if (categoryIds != null){
            transactionsPage = transactionRepository
                    .findByUserIdAndCreatedAtBetweenAndCategoryIdInAndIsDeletedFalse(userId, startOfMonth, endOfMonth, categoryIds, pageable);
        }else {
            transactionsPage = transactionRepository
                    .findByUserIdAndCreatedAtBetweenAndIsDeletedFalse(userId, startOfMonth, endOfMonth, pageable);
        }

        return transactionsPage.map(mapper::mapToTransactionReadOnlyDTO);
    }

//    @Transactional
//    public Page<TransactionReadOnlyDTO> getPaginatedTransactionsByCategory
//            (int page, int size, int month, int year, CategoryType categoryType) {
//
//        String defaultSort = "id";
//        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
//
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDateTime startOfMonth = startDate.atStartOfDay();
//        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
//        LocalDateTime endOfMonth = lastDay.atTime(LocalTime.MAX);
//
//        Page<Transaction> transactions;
//
//        if(categoryType != null) {
//            List<Category> categories = categoryRepository.findByType(categoryType);
//            transactions = transactionRepository.findByCreatedAtBetweenAndCategoryInAndIsDeletedFalse(startOfMonth, endOfMonth, categories, pageable);
//        }else {
//            transactions = transactionRepository.findByCreatedAtBetweenAndIsDeletedFalse(startOfMonth, endOfMonth, pageable);
//        }
//        return transactions.map(mapper::mapToTransactionReadOnlyDTO);
//    }
}
