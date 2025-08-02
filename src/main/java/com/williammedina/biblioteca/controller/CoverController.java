package com.williammedina.biblioteca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping(value = "/covers")
@Tag(name = "Covers", description = "Endpoints for managing book cover images")
public class CoverController {

    @Operation(
            summary = "Get cover image",
            description = "Allows retrieving the cover image of a book stored on the server.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cover image successfully retrieved", content = @Content(mediaType = "image/jpeg")),
            }
    )
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable String filename) {
        try {
            // Define el directorio de las imágenes
            Path filePath = Paths.get("uploads/covers").resolve(filename).normalize();

            // Cargar el archivo como un recurso
            Resource resource = new UrlResource(filePath.toUri());

            // Verificar si el recurso existe y es legible
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("Cover image not found or unreadable: {}", filename);
                return ResponseEntity.notFound().build();
            }

            // Determinar el tipo MIME
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
                log.debug("Could not determine content type for file: {}", filename);
            }

            // Devolver la imagen con el tipo MIME adecuado
            log.info("Cover image found and served: {}", filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Error retrieving cover image '{}': {}", filename, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
