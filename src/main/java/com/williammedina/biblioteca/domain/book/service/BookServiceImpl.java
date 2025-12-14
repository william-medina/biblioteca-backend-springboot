package com.williammedina.biblioteca.domain.book.service;

import com.williammedina.biblioteca.domain.book.dto.*;
import com.williammedina.biblioteca.domain.book.entity.BookEntity;
import com.williammedina.biblioteca.domain.book.repository.BookRepository;
import com.williammedina.biblioteca.domain.book.service.cover.BookCoverService;
import com.williammedina.biblioteca.domain.book.service.finder.BookFinder;
import com.williammedina.biblioteca.domain.book.service.location.BookLocationService;
import com.williammedina.biblioteca.domain.book.service.utils.BookUtils;
import com.williammedina.biblioteca.domain.book.service.validator.BookValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookFinder bookFinder;
    private final BookValidator validator;
    private final BookCoverService bookCoverService;
    private final BookLocationService bookLocationService;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks(String sortBy) {
        log.debug("Getting all books sorted by: {}", sortBy);

        List<BookEntity> books = switch (sortBy) {
            case "author" -> bookRepository.findAllOrderByAuthor();
            case "publisher" -> bookRepository.findAllOrderByPublisher();
            case "publication_year" -> bookRepository.findAllOrderByPublicationYear();
            case "id" -> bookRepository.findAllOrderById();
            default -> bookRepository.findAllOrderByTitle();
        };
        return books.stream().map(BookDTO::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByKeyword(String keyword) {
        log.debug("Getting books by keyword: {}", keyword);
        if (keyword.equals("+")) {
            return bookRepository.findAll().stream().map(BookDTO::fromEntity).toList();
        } else {
            return bookRepository.findByKeyword(keyword).stream().map(BookDTO::fromEntity).toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookByISBN(Long isbn) {
        log.debug("Getting details for book with ISBN: {}", isbn);
        BookEntity book = bookFinder.findBookByIsbn(isbn);
        return BookDTO.fromEntity(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookCountDTO getBookCount() {
        log.debug("Getting the total number of books stored");
        Long count = bookRepository.count();
        return new BookCountDTO(count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getRandomBooks(Long count) {
        log.debug("Getting ({}) random books", count);
        return bookRepository.findRandomBooks(count).stream().map(BookDTO::fromEntity).toList();
    }

    @Override
    @Transactional
    public String addNewBook(InputBookDTO data) {
        log.info("Adding new book");

        validator.ensureIsbnIsUnique(data.isbn());
        validator.ensureLocationIsAvailable(data.location());

        BookEntity book = new BookEntity();
        book.setIsbn(data.isbn());
        book.setTitle(data.title());
        book.setAuthor(BookUtils.normalizeOrDefault(data.author(), "S.A"));
        book.setPublisher(BookUtils.normalizeOrDefault(data.publisher(), "S.E"));
        book.setPublicationYear(BookUtils.normalizeOrDefault(data.publication_year(), "S.F"));
        book.setLocation(BookUtils.normalizeOrDefault(data.location(), "---"));

        // Guardar el libro en la base de datos
        bookRepository.save(book);
        bookCoverService.saveCover(data.cover(), data.isbn());

        return "Libro almacenado correctamente";
    }

    @Override
    @Transactional
    public String updateBook(InputBookDTO data, Long isbn) {
        log.info("Updating book");
        BookEntity bookToUpdate = bookFinder.findBookByIsbn(isbn);

        if (!bookToUpdate.getIsbn().equals(data.isbn())) {
            validator.ensureIsbnIsUnique(data.isbn());
        }

        if (!bookToUpdate.getLocation().equals(data.location())) {
            validator.ensureLocationIsAvailable(data.location());
        }

        bookToUpdate.setIsbn(data.isbn());
        bookToUpdate.setTitle(data.title());
        bookToUpdate.setAuthor(BookUtils.normalizeOrDefault(data.author(), "S.A"));
        bookToUpdate.setPublisher(BookUtils.normalizeOrDefault(data.publisher(), "S.E"));
        bookToUpdate.setPublicationYear(BookUtils.normalizeOrDefault(data.publication_year(), "S.F"));
        bookToUpdate.setLocation(BookUtils.normalizeOrDefault(data.location(), "---"));

        // actualizar el libro en la base de datos
        bookRepository.save(bookToUpdate);
        bookCoverService.saveCover(data.cover(), data.isbn());

        return "Libro actualizado correctamente";
    }

    @Override
    @Transactional
    public void deleteBook(Long isbn) {
        log.info("Deleting book");
        BookEntity book = bookFinder.findBookByIsbn(isbn);
        bookCoverService.deleteCover(book.getIsbn());
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> getLocationBooks() {
        log.debug("Getting book location structure");
        List<BookEntity> books = bookRepository.findAll();
        return bookLocationService.organizeBookLocations(books);
    }

}
