package com.williammedina.biblioteca.controller;


import com.williammedina.biblioteca.domain.book.BookService;
import com.williammedina.biblioteca.domain.book.dto.BookCountDTO;
import com.williammedina.biblioteca.domain.book.dto.BookDTO;
import com.williammedina.biblioteca.domain.book.dto.InputBookDTO;
import com.williammedina.biblioteca.domain.book.dto.LocationDTO;
import com.williammedina.biblioteca.infrastructure.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/books", produces = "application/json")
@Tag(name = "Books", description = "Endpoints for managing book information, including search, registration, updating, and deletion.")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
         this.bookService = bookService;
     }

    @Operation(
            summary = "Get all books sorted by a specific criterion",
            description = "Returns a list of books sorted by a specified criterion, such as 'author', 'publisher', 'publication_year', or 'title' (default).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books successfully retrieved")
            }
    )
    @GetMapping("/{sortBy}")
    public ResponseEntity<List<BookDTO>> getBooks(@PathVariable String sortBy) {
        List<BookDTO> books = bookService.getAllBooks(sortBy);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Search for books by keyword",
            description = "Returns a list of books whose title, author, publisher, or location matches the provided keyword.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books successfully retrieved")
            }
    )
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<BookDTO>> getBooksByKeyword(@PathVariable String keyword) {
        List<BookDTO> books = bookService.getBooksByKeyword(keyword);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Get a book by ISBN",
            description = "Returns the information of a book corresponding to the provided ISBN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByISBN(@PathVariable Long isbn) {
        BookDTO books = bookService.getBookByISBN(isbn);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Get the total number of books",
            description = "Returns the total number of books registered in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Total number of books successfully retrieved")
            }
    )
    @GetMapping("/count")
    public ResponseEntity<BookCountDTO> getBookCount() {
        BookCountDTO count = bookService.getBookCount();
        return ResponseEntity.ok(count);
    }

    @Operation(
            summary = "Get a random list of books",
            description = "Returns a random list of books, based on the requested number.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books successfully retrieved")
            }
    )
    @GetMapping("/random/{count}")
    public ResponseEntity<List<BookDTO>> getRandomBooks(@PathVariable Long count) {
        List<BookDTO> books = bookService.getRandomBooks(count);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Add a new book",
            description = "Registers a new book with the provided information, including an optional cover image.",
            security = @SecurityRequirement(name = "bearer-key"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book successfully stored"),
                    @ApiResponse(responseCode = "409", description = "Conflict while registering the book (ISBN or location already in use)")
            }
    )
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> addNewBook(@Valid @ModelAttribute InputBookDTO data) {
         String message = bookService.addNewBook(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Operation(
            summary = "Update book information",
            description = "Updates the information of an existing book based on the provided ISBN.",
            security = @SecurityRequirement(name = "bearer-key"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @PutMapping(value = "/{isbn}", consumes = "multipart/form-data")
    public ResponseEntity<String> updateBook(@Valid @ModelAttribute InputBookDTO data, @PathVariable Long isbn) {
        String message = bookService.updateBook(data, isbn);
        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes an existing book based on the provided ISBN.",
            security = @SecurityRequirement(name = "bearer-key"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book successfully deleted", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Get the location of books",
            description = "Returns a structured list of books grouped by shelf, section, and position.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Locations successfully retrieved")
            }
    )
    @GetMapping("/location")
    public ResponseEntity<List<LocationDTO>> getLocationBooks() {
        List<LocationDTO> locationBooks = bookService.getLocationBooks();
        return ResponseEntity.ok(locationBooks);
    }
}
