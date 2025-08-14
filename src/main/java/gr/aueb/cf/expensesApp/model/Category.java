package gr.aueb.cf.expensesApp.model;

import gr.aueb.cf.expensesApp.core.enums.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String category_name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private String subcategory;
    private String description;

}
