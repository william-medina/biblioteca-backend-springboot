package com.williammedina.biblioteca.domain.book.dto;

public record BookDTO(
        Long id,
        Long isbn,
        String title,
        String author,
        String publisher,
        String publication_year,
        String location
) {
}
