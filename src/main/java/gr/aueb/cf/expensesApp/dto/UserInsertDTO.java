package gr.aueb.cf.expensesApp.dto;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInsertDTO {

    @NotEmpty(message = "Username cannot be empty.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

//    @NotEmpty(message = "Confirm password cannot be empty.")
//    @Constraint(validatedBy = PasswordMatchValidator.class)
//    @Size(min = 6, message = "Password must have at least 6 characters")
//    private String confirmPassword;
}
