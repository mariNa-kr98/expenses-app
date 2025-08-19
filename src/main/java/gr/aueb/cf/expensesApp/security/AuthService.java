package gr.aueb.cf.expensesApp.security;

import gr.aueb.cf.expensesApp.core.enums.ErrorCode;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.expensesApp.dto.AuthenticationResponseDTO;
import gr.aueb.cf.expensesApp.model.User;
import gr.aueb.cf.expensesApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

   public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) {

       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

       User user = userRepository.findByUsername(authentication.getName())
               .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "User not found."));

       String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
       return new AuthenticationResponseDTO(user.getUsername(), token);
   }
}
