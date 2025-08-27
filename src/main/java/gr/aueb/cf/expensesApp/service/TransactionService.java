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
import org.springframework.beans.factory.annotation.Autowired;
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
        transaction.setMonth(transactionInsertDTO.getMonth());
        transaction.setYear(transactionInsertDTO.getYear());
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
                    if (dto.getMonth() != null) {
                        transaction.setMonth(dto.getMonth());
                    }
                    if (dto.getYear() != null) {
                        transaction.setYear(dto.getYear());
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
        return "Transaction with id " + transactionId + " was soft-deleted succesfully.";
    }

    @Transactional
    public Page<TransactionReadOnlyDTO> getPaginatedTransactions
            (Long userId,
             int page,
             int size,
             Integer month,
             Integer year,
             boolean includeDeleted,
             @Nullable Long categoryId,
             @Nullable CategoryType categoryType) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        Page<Transaction> transactionsPage = transactionRepository.findTransactionsFiltered(
                userId,
                month,
                year,
                categoryId,
                categoryType,
                includeDeleted,
                pageable);

        return transactionsPage.map(mapper::mapToTransactionReadOnlyDTO);
    }
}
