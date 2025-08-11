package gr.aueb.cf.expensesApp.rest;

import gr.aueb.cf.expensesApp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.expensesApp.dto.AuthenticationResponseDTO;
import gr.aueb.cf.expensesApp.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login( @Valid @RequestBody AuthenticationRequestDTO request) {
        AuthenticationResponseDTO response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
