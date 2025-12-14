package com.williammedina.biblioteca.controller;

import com.williammedina.biblioteca.domain.book.service.cover.BookCoverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(value = "/covers")
@Tag(name = "Covers", description = "Endpoints for managing book cover images")
@AllArgsConstructor
public class CoverController {

    private final BookCoverService bookCoverService;

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
            Resource resource = bookCoverService.loadCover(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
