package com.williammedina.biblioteca.domain.user.service;

import com.williammedina.biblioteca.domain.user.dto.LoginUserDTO;
import com.williammedina.biblioteca.domain.user.dto.UserDTO;
import com.williammedina.biblioteca.domain.user.entity.UserEntity;
import com.williammedina.biblioteca.domain.user.repository.UserRepository;
import com.williammedina.biblioteca.infrastructure.exception.AppException;
import com.williammedina.biblioteca.infrastructure.security.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public String authenticateAndGenerateToken(LoginUserDTO data) {
        log.info("Attempting to authenticate user: {}", data.email());

        checkIfUserExists(data.email());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        UserEntity user = (UserEntity) authenticatedUser.getPrincipal();

        log.info("User authenticated successfully. ID: {}", user.getId());

        return tokenService.generateToken((UserEntity) authenticatedUser.getPrincipal());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        UserEntity user = getAuthenticatedUser();
        log.debug("Retrieving user data. ID: {}", user.getId());
        return UserDTO.fromEntity(user);
    }

    private void checkIfUserExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            log.error("Email not registered: {}", email);
            throw new AppException("Usuario no registrado", HttpStatus.CONFLICT);
        }
    }

    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserEntity) {
            return (UserEntity) authentication.getPrincipal();
        }

        log.error("Failed to retrieve a valid authenticated user");
        throw new IllegalStateException("El usuario autenticado no es válido.");
    }

}
