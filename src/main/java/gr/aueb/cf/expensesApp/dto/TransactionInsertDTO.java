package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.model.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionInsertDTO {

    @NotEmpty(message = "Amount cannot be empty.")
    private Double amount;

    @NotEmpty(message = "Please, choose a category.")
    private CategoryReadOnlyDTO categoryReadOnlyDTO;
}
