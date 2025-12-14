package com.williammedina.biblioteca.domain.user.service;

import com.williammedina.biblioteca.domain.user.dto.LoginUserDTO;
import com.williammedina.biblioteca.domain.user.dto.UserDTO;
import com.williammedina.biblioteca.domain.user.entity.UserEntity;
import com.williammedina.biblioteca.domain.user.service.context.AuthenticatedUserProvider;
import com.williammedina.biblioteca.domain.user.service.validator.UserValidator;
import com.williammedina.biblioteca.infrastructure.security.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserValidator validator;

    @Override
    @Transactional
    public String authenticateAndGenerateToken(LoginUserDTO data) {
        log.info("Attempting to authenticate user: {}", data.email());

        validator.checkIfUserExists(data.email());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        UserEntity user = (UserEntity) authenticatedUser.getPrincipal();

        log.info("User authenticated successfully. ID: {}", user.getId());

        return tokenService.generateToken((UserEntity) authenticatedUser.getPrincipal());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        UserEntity currentUser = authenticatedUserProvider.getAuthenticatedUser();
        log.debug("Retrieving user data. ID: {}", currentUser.getId());
        return UserDTO.fromEntity(currentUser);
    }

}
