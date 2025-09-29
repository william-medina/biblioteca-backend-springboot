package com.williammedina.biblioteca.controller;

import com.williammedina.biblioteca.domain.user.service.UserService;
import com.williammedina.biblioteca.domain.user.dto.LoginUserDTO;
import com.williammedina.biblioteca.domain.user.dto.UserDTO;
import com.williammedina.biblioteca.infrastructure.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@Tag(name = "Auth", description = "Endpoints for user authentication.")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Log in",
            description = "Authenticates the user and generates a JWT token for authenticated sessions.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(example = "token"))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "User does not exist or incorrect password", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginUserDTO data){
        String responseToken = userService.authenticateAndGenerateToken(data);
        return ResponseEntity.ok(responseToken);
    }

    @Operation(
            summary = "Get authenticated user information",
            description = "Retrieves details of the currently authenticated user.",
            security = @SecurityRequirement(name = "bearer-key"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid bearer token", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
}
