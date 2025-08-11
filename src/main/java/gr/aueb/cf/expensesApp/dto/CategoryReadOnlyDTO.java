package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryReadOnlyDTO {

    private CategoryType categoryType;
    private List<String> subcategories;

}
