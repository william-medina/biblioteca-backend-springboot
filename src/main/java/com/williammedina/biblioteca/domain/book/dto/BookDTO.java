package com.williammedina.biblioteca.domain.book.dto;

import com.williammedina.biblioteca.domain.book.entity.BookEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO containing general book data")
public record BookDTO(

        @Schema(description = "Unique identifier of the book", example = "1")
        Long id,

        @Schema(description = "ISBN number of the book", example = "9781234567897")
        Long isbn,

        @Schema(description = "Title of the book", example = "Soil Fertility")
        String title,

        @Schema(description = "Author of the book", example = "Henry D. Foth")
        String author,

        @Schema(description = "Publisher of the book", example = "Lewis")
        String publisher,

        @Schema(description = "Year the book was published", example = "1997")
        String publication_year,

        @Schema(description = "Shelf location of the book in the library", example = "P-A12")
        String location
) {

    public static BookDTO fromEntity(BookEntity book) {
        return new BookDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getLocation()
        );
    }
}
