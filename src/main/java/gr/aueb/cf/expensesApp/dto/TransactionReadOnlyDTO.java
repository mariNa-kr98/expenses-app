package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.model.Category;
import gr.aueb.cf.expensesApp.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionReadOnlyDTO {

    private Long id;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private UserReadOnlyDTO user;
    private Category category;
    private Integer month;
    private Integer year;
    private String notes;
}
