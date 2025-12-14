package com.williammedina.biblioteca.domain.book.service.validator;

import com.williammedina.biblioteca.domain.book.repository.BookRepository;
import com.williammedina.biblioteca.infrastructure.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookValidatorImpl implements BookValidator {

    private final BookRepository bookRepository;

    @Override
    public void ensureIsbnIsUnique(Long isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            log.warn("A book with ISBN {} already exists", isbn);
            throw new AppException("Un libro con ese ISBN ya existe.", HttpStatus.CONFLICT);
        }
    }

    @Override
    public void ensureLocationIsAvailable(String location) {
        if (bookRepository.existsByLocation(location)) {
            log.warn("A book already exists at location: {}", location);
            throw new AppException("Un libro ya tiene esa ubicación.", HttpStatus.CONFLICT);
        }
    }
}
