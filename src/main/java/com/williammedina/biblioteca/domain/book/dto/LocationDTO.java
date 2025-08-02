package com.williammedina.biblioteca.domain.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO representing a bookshelf with its sections")
public record LocationDTO(

        @Schema(description = "Shelf identifier", example = "P")
        String shelf,

        @Schema(description = "List of sections in the shelf")
        List<SectionDTO> sections
) {
}
