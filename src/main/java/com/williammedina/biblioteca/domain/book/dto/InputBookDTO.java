package com.williammedina.biblioteca.domain.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "DTO for receiving book creation input")
public record InputBookDTO(

        @Schema(description = "ISBN number of the book", requiredMode = Schema.RequiredMode.REQUIRED, example = "9781234567897")
        @NotNull(message = "El ISBN es obligatorio")
        Long isbn,

        @Schema(description = "Title of the book", example = "Soil Fertility")
        @NotBlank(message = "El título es obligatorio")
        String title,

        @Schema(description = "Author of the book", example = "Henry D. Foth")
        String author,

        @Schema(description = "Publisher of the book", example = "Lewis")
        String publisher,

        @Schema(description = "Year the book was published", example = "1997")
        @Size(max = 6, message = "El año de publicación no puede tener más de 6 caracteres")
        String publication_year,

        @Schema(description = "Shelf location of the book in the library", example = "P-A12")
        @Size(max = 6, message = "La ubicación no puede tener más de 6 caracteres")
        String location,

        @Schema(description = "Cover image of the book (multipart file)")
        MultipartFile cover
) {
}
