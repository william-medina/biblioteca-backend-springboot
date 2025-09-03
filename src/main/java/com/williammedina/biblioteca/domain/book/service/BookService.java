package com.williammedina.biblioteca.domain.book.service;

import com.williammedina.biblioteca.domain.book.dto.*;

import java.util.*;

public interface BookService {

    List<BookDTO> getAllBooks(String sortBy);
    List<BookDTO> getBooksByKeyword(String keyword);
    BookDTO getBookByISBN(Long isbn);
    BookCountDTO getBookCount();
    List<BookDTO> getRandomBooks(Long count);
    String addNewBook(InputBookDTO data);
    String updateBook(InputBookDTO data, Long isbn);
    void deleteBook(Long isbn);
    List<LocationDTO> getLocationBooks();

}
