package gr.aueb.cf.expensesApp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionInsertDTO {

    @NotNull(message = "Amount cannot be empty.")
    private Double amount;

    @Valid
    @NotNull(message = "Please, choose a category.")
    private CategoryReadOnlyDTO categoryReadOnlyDTO;
}
