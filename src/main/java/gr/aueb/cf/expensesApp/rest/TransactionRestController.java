package gr.aueb.cf.expensesApp.rest;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.core.enums.ErrorCode;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.dto.TransactionInsertDTO;
import gr.aueb.cf.expensesApp.dto.TransactionReadOnlyDTO;
import gr.aueb.cf.expensesApp.dto.TransactionUpdateDTO;
import gr.aueb.cf.expensesApp.mapper.Mapper;
import gr.aueb.cf.expensesApp.model.Transaction;
import gr.aueb.cf.expensesApp.model.User;
import gr.aueb.cf.expensesApp.repository.UserRepository;
import gr.aueb.cf.expensesApp.service.CategoryService;
import gr.aueb.cf.expensesApp.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.IMessage;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;
    private final Mapper mapper;
    private final UserRepository userRepository;


    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "User not found"));
        return user.getId();
    }

    @PostMapping("/transactions/save")
    public ResponseEntity<TransactionReadOnlyDTO> saveTransaction
            (@Valid @RequestBody TransactionInsertDTO transactionInsertDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TransactionReadOnlyDTO responseDTO = transactionService.saveTransaction(transactionInsertDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/transactions/modify/{id}")
    public ResponseEntity<TransactionReadOnlyDTO> modifyTransaction
            (@PathVariable("id") Long transactionId,
             @Valid @RequestBody TransactionUpdateDTO transactionUpdateDTO) {

        Long userId = getCurrentUserId();
        Optional<Transaction> updatedTransaction = transactionService.modifyTransaction
                (transactionId, userId, transactionUpdateDTO);

        return updatedTransaction
                .map(updated -> {
                    TransactionReadOnlyDTO responseDTO = mapper.mapToTransactionReadOnlyDTO(updated);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                })
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "Transaction not found"));

    }

    @DeleteMapping("/transactions/delete/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable("id") Long transactionId) {
        Long userId = getCurrentUserId();
        String message = transactionService.deleteTransaction(transactionId, userId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("transactions/paginated")
    public ResponseEntity<Page<TransactionReadOnlyDTO>> getPaginatedTransactions
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "5") int size,
             @RequestParam int month,
             @RequestParam int year,
             @RequestParam(required = false) List<Long> categoryIds,
             @RequestParam(required = false) Long categoryId,
             @RequestParam(required = false) CategoryType categoryType) {

        Long userId = getCurrentUserId();

        Page<TransactionReadOnlyDTO> transactionsPage = transactionService
                .getPaginatedTransactions(userId, page, size, month, year, categoryIds, categoryId, categoryType);
        return ResponseEntity.ok(transactionsPage);
    }
}
