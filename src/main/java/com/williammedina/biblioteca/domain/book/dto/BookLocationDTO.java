package com.williammedina.biblioteca.domain.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing book data along with its physical location and quantity")
public record BookLocationDTO(

        @Schema(description = "Book ID", example = "1")
        Long id,

        @Schema(description = "ISBN of the book", example = "9789876543210")
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
        String location,

        @Schema(description = "Book position from left to right within the section", example = "12")
        Integer number
) {
}
