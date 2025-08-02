package com.williammedina.biblioteca.infrastructure.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "General Error Response")
public class ErrorResponse {
    @Schema(description = "Descriptive error message", example = "Book not found")
    private String error;
}
