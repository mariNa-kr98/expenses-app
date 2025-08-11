package gr.aueb.cf.expensesApp.rest;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.dto.TransactionInsertDTO;
import gr.aueb.cf.expensesApp.dto.TransactionReadOnlyDTO;
import gr.aueb.cf.expensesApp.mapper.Mapper;
import gr.aueb.cf.expensesApp.model.Transaction;
import gr.aueb.cf.expensesApp.service.CategoryService;
import gr.aueb.cf.expensesApp.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;
    private final Mapper mapper;
    private final CategoryService categoryService;


    @PostMapping("/transactions/save")
    public ResponseEntity<TransactionReadOnlyDTO> saveTransaction
            (@Valid TransactionInsertDTO transactionInsertDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TransactionReadOnlyDTO responseDTO = transactionService.saveTransaction(transactionInsertDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/transactions/modify/{id}")
    public ResponseEntity<TransactionReadOnlyDTO> modifyTransaction
            (@PathVariable("id") Long transactionId,
             @Valid @RequestBody TransactionInsertDTO transactionInsertDTO) {

        Transaction transaction = mapper.mapToTransactionEntity(transactionInsertDTO);
        Optional<Transaction> updatedTransaction = transactionService.modifyTransaction(transactionId, transaction);

        TransactionReadOnlyDTO responseDTO = mapper.mapToTransactionReadOnlyDTO(updatedTransaction.get());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @DeleteMapping("/transactions/delete/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("transactions/month/paginated")
    public ResponseEntity<Page<TransactionReadOnlyDTO>> getPaginatedTransactions
            (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
             @RequestParam int month, @RequestParam int year) {

        Page<TransactionReadOnlyDTO> transactionsPage = transactionService.getPaginatedTransactions(page, size, month, year);
        return new ResponseEntity<>(transactionsPage, HttpStatus.OK);

    }

    @GetMapping("/transactions/category/paginated")
    public ResponseEntity<Page<TransactionReadOnlyDTO>> getPaginatedTransactionsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(required = false) CategoryType categoryType) {

        Page<TransactionReadOnlyDTO> transactionsPage =
                transactionService.getPaginatedTransactionsByCategory(page, size, month, year, categoryType);

        return new ResponseEntity<>(transactionsPage, HttpStatus.OK);
    }
}
