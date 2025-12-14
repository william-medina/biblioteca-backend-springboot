package com.williammedina.biblioteca.infrastructure.security;

import com.williammedina.biblioteca.domain.user.entity.UserEntity;
import com.williammedina.biblioteca.domain.user.repository.UserRepository;
import com.williammedina.biblioteca.domain.user.service.context.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService, AuthenticatedUserProvider {

    private final UserRepository userRepository;

    @Override
    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserEntity) {
            return (UserEntity) authentication.getPrincipal();
        }

        log.error("Failed to retrieve a valid authenticated user");
        throw new IllegalStateException("El usuario autenticado no es válido.");
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("Usuario con el email '%s' no encontrado", email)
                        )
                );
    }

}
