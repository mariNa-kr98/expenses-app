package gr.aueb.cf.expensesApp.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserReadOnlyDTO {

    private String username;
    private Long id;
    private String role;
}
