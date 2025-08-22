package gr.aueb.cf.expensesApp.service;

import gr.aueb.cf.expensesApp.core.enums.ErrorCode;
import gr.aueb.cf.expensesApp.core.enums.Role;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.dto.UserInsertDTO;
import gr.aueb.cf.expensesApp.dto.UserReadOnlyDTO;
import gr.aueb.cf.expensesApp.mapper.Mapper;
import gr.aueb.cf.expensesApp.model.User;
import gr.aueb.cf.expensesApp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) {

        if (userRepository.findByUsername(userInsertDTO.getUsername()).isPresent()){
            throw new AppException(ErrorCode.ENTITY_ALREADY_EXISTS, "User already exists.");
        }

        User user = mapper.mapToUserEntity(userInsertDTO);
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        return mapper.mapToUserReadOnlyDTO(savedUser);
    }

    public UserReadOnlyDTO registerAsAdmin(UserInsertDTO dto) {
        User user = mapper.mapToUserEntity(dto);
        user.setRole(Role.ADMIN); // ✅ Set admin role explicitly
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return mapper.mapToUserReadOnlyDTO(userRepository.save(user));
    }



    public User deleteUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND_EXCEPTION, "User not found."));

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.ENTITY_NOT_AUTHORIZED_EXCEPTION, "Only admins can perform this action!");
        }
        userRepository.delete(user);
        return user;
    }
}
