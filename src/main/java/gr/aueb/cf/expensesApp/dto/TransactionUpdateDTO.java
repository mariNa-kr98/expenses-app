package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.model.Category;
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
    private Double amount;
    private Category category;
    private Boolean isDeleted;
}
