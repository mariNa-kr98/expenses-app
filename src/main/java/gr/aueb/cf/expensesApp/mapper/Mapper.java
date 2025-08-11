package gr.aueb.cf.expensesApp.mapper;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
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
        transaction.setAmount(transactionInsertDTO.getAmount());

        Category category = mapToCategory(transactionInsertDTO.getCategoryReadOnlyDTO());
        transaction.setCategory(category);

        return transaction;
    }

    public TransactionReadOnlyDTO mapToTransactionReadOnlyDTO(Transaction transaction){

        return new TransactionReadOnlyDTO(transaction.getId(), transaction.getAmount(),
                transaction.getCreatedAt(), transaction.getUpdatedAt(),
                transaction.getIsDeleted(), transaction.getUser(), transaction.getCategory());
    }

    public Category mapToCategory (CategoryReadOnlyDTO categoryReadOnlyDTO) {

        Category category = new Category();
        CategoryType categoryType = CategoryType.valueOf(categoryReadOnlyDTO.getCategoryType().name());
        category.setType(categoryType);
        category.setDescription(String.join(", ", categoryType.getSubcategories()));

        return category;
    }
}
