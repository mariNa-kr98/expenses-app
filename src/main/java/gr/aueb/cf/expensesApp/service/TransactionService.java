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
import gr.aueb.cf.expensesApp.repository.CategoryRepository;
import gr.aueb.cf.expensesApp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public TransactionReadOnlyDTO saveTransaction(TransactionInsertDTO transactionInsertDTO) {

//        if (transactionRepository.findByIdAndIsDeletedFalse().isPresent()) {
//            throw new AppException(ErrorCode.ENTITY_ALREADY_EXISTS,  "Transaction already exists!");
//        }

        Transaction transaction = mapper.mapToTransactionEntity(transactionInsertDTO);
        transaction.setAmount(transactionInsertDTO.getAmount());
        Category category = mapper.mapToCategory(transactionInsertDTO.getCategoryReadOnlyDTO());
        transaction.setCategory(category);
        transaction.setIsDeleted(false);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapper.mapToTransactionReadOnlyDTO(savedTransaction);

    }

    @Transactional
    public Optional<Transaction> modifyTransaction
            (Long transactionId, Transaction updatedTransaction){

        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Transaction not found.");
        }

        return optionalTransaction.map(transaction ->
                    {transaction.setAmount(updatedTransaction.getAmount());
                    transaction.setCategory(updatedTransaction.getCategory());
         return transactionRepository.save(transaction);
                 });
    }

    @Transactional
    public void deleteTransaction(Long transactionId){

        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Transaction not found.");
        }

        transactionRepository.deleteById(transactionId);
    }

    @Transactional
    public Page<TransactionReadOnlyDTO> getPaginatedTransactions (int page, int size, int month, int year) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = startDate.atStartOfDay();

        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(LocalTime.MAX);

        return transactionRepository
                .findByCreatedAtBetween(startOfMonth, endOfMonth, pageable)
                .map(mapper::mapToTransactionReadOnlyDTO);
    }

    @Transactional
    public Page<TransactionReadOnlyDTO> getPaginatedTransactionsByCategory
            (int page, int size, int month, int year, CategoryType categoryType) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonth = startDate.atStartOfDay();
        LocalDate lastDay = startDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(LocalTime.MAX);

        Page<Transaction> transactions;

        if(categoryType != null) {
            List<Category> categories = categoryRepository.findByType(categoryType);
            transactions = transactionRepository.findByCreatedAtBetweenAndCategoryIn(startOfMonth, endOfMonth, categories, pageable);
        }else {
            transactions = transactionRepository.findByCreatedAtBetween(startOfMonth, endOfMonth, pageable);
        }
        return transactions.map(mapper::mapToTransactionReadOnlyDTO);
    }
}
