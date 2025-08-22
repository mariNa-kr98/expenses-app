package gr.aueb.cf.expensesApp.rest;

import gr.aueb.cf.expensesApp.dto.UserInsertDTO;
import gr.aueb.cf.expensesApp.dto.UserReadOnlyDTO;
import gr.aueb.cf.expensesApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")//convention
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    //public access
    @PostMapping("/users/save")
    public ResponseEntity<UserReadOnlyDTO> saveUser
            (@Valid @RequestBody UserInsertDTO userInsertDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println(error.getField() + ": " + error.getDefaultMessage());

            });
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
        return new ResponseEntity<>(userReadOnlyDTO, HttpStatus.OK);
    }

    //admin access
    @PostMapping("/admins/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserReadOnlyDTO> saveAdmin(@Valid @RequestBody UserInsertDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();

        UserReadOnlyDTO user = userService.registerAsAdmin(dto);
        return ResponseEntity.ok(user);
    }

    //admin access
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<UserReadOnlyDTO> deleteUser(@PathVariable("id") Long userId){

            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();

    }
}

