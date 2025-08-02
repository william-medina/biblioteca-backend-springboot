package com.williammedina.biblioteca.domain.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO representing a section in a shelf with its books")
public record SectionDTO(

        @Schema(description = "Section identifier", example = "A")
        String section,

        @Schema(description = "List of books in this section")
        List<BookLocationDTO> books
) {
}
