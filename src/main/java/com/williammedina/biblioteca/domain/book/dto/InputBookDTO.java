package com.williammedina.biblioteca.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record InputBookDTO(
        @NotNull(message = "El ISBN es obligatorio")
        Long isbn,

        @NotBlank(message = "El título es obligatorio")
        String title,

        String author,

        String publisher,

        @Size(max = 6, message = "El año de publicación no puede tener más de 6 caracteres")
        String publication_year,

        @Size(max = 6, message = "La ubicación no puede tener más de 6 caracteres")
        String location,

        MultipartFile cover
) {
}
