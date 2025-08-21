package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.model.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionUpdateDTO {

    private Long id;

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    private Double amount;
    private Long categoryId;
    private Boolean isDeleted;

    @Size(max = 255, message = "Notes cannot exceed 255 characters.")
    private String notes;
}
