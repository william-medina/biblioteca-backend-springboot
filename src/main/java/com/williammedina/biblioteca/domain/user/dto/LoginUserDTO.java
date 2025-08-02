package com.williammedina.biblioteca.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login Data")
public record LoginUserDTO(

        @Schema(description = "User's email", example = "user@email.com")
        @NotBlank(message = "El email es requerido.")
        String email,

        @Schema(description = "Password", example = "myPassword123")
        @NotBlank(message = "El password es requerido.")
        String password
) {
}
