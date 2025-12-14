package com.williammedina.biblioteca.domain.book.service.finder;

import com.williammedina.biblioteca.domain.book.entity.BookEntity;
import com.williammedina.biblioteca.domain.book.repository.BookRepository;
import com.williammedina.biblioteca.infrastructure.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookFinderImpl implements BookFinder {

    private final BookRepository bookRepository;

    @Override
    public BookEntity findBookByIsbn(Long isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> {
                    log.error("Book not found with ISBN: {}", isbn);
                    return new AppException("Book not found.", HttpStatus.NOT_FOUND);
                });
    }
}
