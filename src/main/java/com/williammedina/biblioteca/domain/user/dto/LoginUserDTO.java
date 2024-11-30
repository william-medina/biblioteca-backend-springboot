package com.williammedina.biblioteca.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserDTO(
        @NotBlank(message = "El email es requerido.")
        String email,

        @NotBlank(message = "El password es requerido.")
        String password
) {
}
