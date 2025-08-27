package gr.aueb.cf.expensesApp.mapper;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import gr.aueb.cf.expensesApp.core.enums.Role;
import gr.aueb.cf.expensesApp.dto.*;
import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.model.Transaction;
import gr.aueb.cf.expensesApp.model.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public User mapToUserEntity(UserInsertDTO userInsertDTO) {

        User user = new User();
        user.setUsername(userInsertDTO.getUsername());
        user.setPassword(userInsertDTO.getPassword());
        if (userInsertDTO.getRole() != null) {
            user.setRole(userInsertDTO.getRole());
        } else {
            user.setRole(Role.USER);
        }
        return user;
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {

        UserReadOnlyDTO userReadOnlyDTO = new UserReadOnlyDTO();

        userReadOnlyDTO.setUsername(user.getUsername());
        userReadOnlyDTO.setId(user.getId());

        return userReadOnlyDTO;
    }

    public Transaction mapToTransactionEntity(TransactionInsertDTO transactionInsertDTO) {

        Transaction transaction = new Transaction();
        transaction.setMonth(transactionInsertDTO.getMonth());
        transaction.setYear(transactionInsertDTO.getYear());
        transaction.setAmount(transactionInsertDTO.getAmount());
        transaction.setNotes(transactionInsertDTO.getNotes());

        return transaction;
    }

    public TransactionReadOnlyDTO mapToTransactionReadOnlyDTO(Transaction transaction){

        UserReadOnlyDTO userReadOnlyDTO = mapToUserReadOnlyDTO(transaction.getUser());

        return new TransactionReadOnlyDTO(transaction.getId(), transaction.getAmount(),
                transaction.getCreatedAt(), transaction.getUpdatedAt(),
                transaction.getIsDeleted(), userReadOnlyDTO, transaction.getCategory(),
                transaction.getMonth(), transaction.getYear(), transaction.getNotes());
    }

    public Category mapToCategory (CategoryInsertDTO categoryInsertDTO) {

        Category category = new Category();
        category.setType(categoryInsertDTO.getCategoryType());
        category.setLabel(categoryInsertDTO.getLabel());

        return category;
    }

    public CategoryReadOnlyDTO mapToCategoryReadOnlyDTO(Category category) {
        CategoryReadOnlyDTO dto = new CategoryReadOnlyDTO();
        dto.setId(category.getId());
        dto.setCategoryType(category.getType());
        dto.setLabel(category.getLabel());
        return dto;
    }
}
