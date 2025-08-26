package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.model.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private Double amount;

    @NotNull(message = "Please, choose a category.")
    private Long categoryId;

    @Size(max = 255)
    private String notes;

    @NotNull(message = "Please, choose a month.")
    private Integer month;

    @NotNull(message = "Please, choose a year.")
    private Integer year;
}
