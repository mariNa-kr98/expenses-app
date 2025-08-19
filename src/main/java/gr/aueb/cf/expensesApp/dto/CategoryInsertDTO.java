package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryInsertDTO {

    @NotNull(message = "Please choose a category type.")
    private CategoryType categoryType;

    @NotNull(message = "Please, choose a label.")
    private String label;
}
