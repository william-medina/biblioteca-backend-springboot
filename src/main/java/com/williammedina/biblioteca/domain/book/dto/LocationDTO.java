package com.williammedina.biblioteca.domain.book.dto;

import java.util.List;

public record LocationDTO(
        String shelf,
        List<SectionDTO> sections
) {
}
