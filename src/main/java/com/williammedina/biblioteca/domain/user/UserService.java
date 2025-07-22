package com.williammedina.biblioteca.domain.user;

import com.williammedina.biblioteca.domain.user.dto.LoginUserDTO;
import com.williammedina.biblioteca.domain.user.dto.UserDTO;
import com.williammedina.biblioteca.infrastructure.exception.AppException;
import com.williammedina.biblioteca.infrastructure.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public String authenticateAndGenerateToken(LoginUserDTO data) {
        checkIfUserExists(data.email());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        return tokenService.generateToken((User) authenticatedUser.getPrincipal());
    }

    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        User user = getAuthenticatedUser();
        return toUserDTO(user);
    }

    private void checkIfUserExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new AppException("Usuario no registrado", HttpStatus.CONFLICT);
        }
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        throw new IllegalStateException("El usuario autenticado no es válido.");
    }

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail()
        );
    }


}
