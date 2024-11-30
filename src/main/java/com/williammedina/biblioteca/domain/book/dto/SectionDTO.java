package com.williammedina.biblioteca.domain.book.dto;

import java.util.List;

public record SectionDTO(
        String section,
        List<BookLocationDTO> books
) {
}
