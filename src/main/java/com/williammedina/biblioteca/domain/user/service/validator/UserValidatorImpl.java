package com.williammedina.biblioteca.domain.user.service.validator;

import com.williammedina.biblioteca.domain.user.repository.UserRepository;
import com.williammedina.biblioteca.infrastructure.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {

    private final UserRepository userRepository;

    @Override
    public void ensureUserExists(String email) {
        if (!userRepository.existsByEmail(email)) {
            log.error("Email not registered: {}", email);
            throw new AppException("Usuario no registrado", HttpStatus.CONFLICT);
        }
    }
}
