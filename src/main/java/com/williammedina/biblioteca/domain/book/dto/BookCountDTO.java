package com.williammedina.biblioteca.domain.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing the total number of books")
public record BookCountDTO(

        @Schema(description = "Total count of books", example = "750")
        Long count
) {
}
