package com.williammedina.biblioteca.domain.book.service.cover;

import com.williammedina.biblioteca.infrastructure.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCoverServiceImpl implements BookCoverService {

    private final String uploadDir = "uploads/covers";

    @Override
    public void saveCover(MultipartFile cover, Long isbn) {

        if (cover == null || cover.isEmpty()) {
            log.debug("No cover file provided for ISBN: {}", isbn);
            return;
        }

        String fileName = getCoverFileName(isbn, cover);
        Path path = Paths.get(uploadDir, fileName);

        try {
            Files.copy(cover.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            log.info("Book cover saved for ISBN: {}", isbn);
        } catch (IOException e) {
            log.error("Failed to save book cover for ISBN {}: {}", isbn, e.getMessage());
            throw new RuntimeException("Error al guardar la portada del libro", e);
        }
    }

    @Override
    public void deleteCover(Long isbn) {
        String fileName = isbn + ".jpg";
        Path path = Paths.get(uploadDir, fileName);

        try {
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("Book cover deleted for ISBN: {}", isbn);
            } else {
                log.warn("No cover found to delete for ISBN: {}", isbn);
            }
        } catch (IOException e) {
            log.error("Failed to delete book cover for ISBN {}: {}", isbn, e.getMessage());
            throw new RuntimeException("Error al eliminar la portada del libro", e);
        }
    }

    @Override
    public String getCoverFileName(Long isbn, MultipartFile cover) {

        String originalFilename = cover.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            log.warn("Invalid file name for ISBN {}: {}", isbn, originalFilename);
            throw new AppException("El archivo de portada no tiene un nombre válido", HttpStatus.BAD_REQUEST);
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!"jpg".equals(extension)) {
            log.error("Invalid file extension for ISBN {}: .{}", isbn, extension);
            throw new AppException("Solo se permiten archivos con extensión .jpg", HttpStatus.BAD_REQUEST);
        }

        return isbn + "." + extension;
    }

    @Override
    public Resource loadCover(String filename) {

        try {
            Path path = Paths.get(uploadDir, filename);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("Cover image not found or unreadable: {}", filename);
                throw new RuntimeException("Cover not found for filename: " + filename);
            }
            return resource;
        } catch (Exception e) {
            log.error("Error retrieving cover image '{}': {}", filename, e.getMessage(), e);
            throw new RuntimeException("Error loading cover for filename: " + filename, e);
        }
    }
}
