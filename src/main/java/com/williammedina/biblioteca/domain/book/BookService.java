package com.williammedina.biblioteca.domain.book;

import com.williammedina.biblioteca.domain.book.dto.*;
import com.williammedina.biblioteca.infrastructure.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class BookService {

    public final BookRepository bookRepository;
    private final String uploadDir = "uploads/covers";

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks(String sortBy) {
        List<Book> books = switch (sortBy) {
            case "author" -> bookRepository.findAllOrderByAuthor();
            case "publisher" -> bookRepository.findAllOrderByPublisher();
            case "publication_year" -> bookRepository.findAllOrderByPublicationYear();
            case "id" -> bookRepository.findAllOrderById();
            default -> bookRepository.findAllOrderByTitle();
        };
        return books.stream().map(this::toBookDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByKeyword(String keyword) {
        if (keyword.equals("+")) {
            return bookRepository.findAll().stream().map(this::toBookDTO).toList();
        } else {
            return bookRepository.findByKeyword(keyword).stream().map(this::toBookDTO).toList();
        }
    }

    @Transactional(readOnly = true)
    public BookDTO getBookByISBN(Long isbn) {
        Book book = findBookByIsbn(isbn);
        return toBookDTO(book);
    }

    @Transactional(readOnly = true)
    public BookCountDTO getBookCount() {
        Long count = bookRepository.count();
        return new BookCountDTO(count);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getRandomBooks(Long count) {
        return bookRepository.findRandomBooks(count).stream().map(this::toBookDTO).toList();
    }


    @Transactional
    public String addNewBook(InputBookDTO data) {

            existsByIsbn(data.isbn());
            existsByLocation(data.location());

            Book book = new Book();
            book.setIsbn(data.isbn());
            book.setTitle(data.title());
            book.setAuthor(validateAndTrim(data.author(), "S.A"));
            book.setPublisher(validateAndTrim(data.publisher(), "S.E"));
            book.setPublicationYear(validateAndTrim(data.publication_year(), "S.F"));
            book.setLocation(validateAndTrim(data.location(), "---"));

            // Guardar el libro en la base de datos
            bookRepository.save(book);

        try {
            // Subir la portada si existe
            if (data.cover() != null && !data.cover().isEmpty()) {
                String fileName = book.getIsbn() + "." + getFileExtension(data.cover());
                Path path = Paths.get(uploadDir, fileName);
                Files.copy(data.cover().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            return "Libro almacenado correctamente";
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la portada", e);
        }
    }

    @Transactional
    public String updateBook(InputBookDTO data, Long isbn) {
        Book book = findBookByIsbn(isbn);

        if (!book.getIsbn().equals(data.isbn())) {
            existsByIsbn(data.isbn());
        }

        if (!book.getLocation().equals(data.location())) {
            existsByLocation(data.location());
        }

        book.setIsbn(data.isbn());
        book.setTitle(data.title());
        book.setAuthor(validateAndTrim(data.author(), "S.A"));
        book.setPublisher(validateAndTrim(data.publisher(), "S.E"));
        book.setPublicationYear(validateAndTrim(data.publication_year(), "S.F"));
        book.setLocation(validateAndTrim(data.location(), "---"));

        // actualizar el libro en la base de datos
        bookRepository.save(book);

        try {
            // Subir la portada si existe
            if (data.cover() != null && !data.cover().isEmpty()) {
                String fileName = book.getIsbn() + "." + getFileExtension(data.cover());
                Path path = Paths.get(uploadDir, fileName);
                Files.copy(data.cover().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            return "Libro actualizado correctamente";
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la portada", e);
        }
    }

    @Transactional
    public void deleteBook(Long isbn) {
        Book book = findBookByIsbn(isbn);

        String fileName = isbn + ".jpg";
        Path filePath = Paths.get(uploadDir, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            bookRepository.delete(book);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo de portada para el libro con ISBN: " + isbn, e);
        }
    }

    @Transactional(readOnly = true)
    public List<LocationDTO> getLocationBooks() {
        List<Book> books = bookRepository.findAll();
        Map<String, LocationDTO> locationBooks = new HashMap<>();

        for (Book book : books) {
            String location = book.getLocation();

            // Validar que la ubicación tenga el formato adecuado
            if (location != null && location.contains("-")) {
                String[] parts = location.split("-");

                // Verificar que la ubicación tenga al menos dos partes (estantería y posición)
                if (parts.length >= 2) {
                    String shelf = parts[0];
                    String position = parts[1];

                    // Obtener la sección y el número de posición
                    if (position.length() > 1) {
                        String section = position.substring(0, 1);
                        int number;

                        try {
                            number = Integer.parseInt(position.substring(1));
                        } catch (NumberFormatException e) {
                            continue; // Si no se puede convertir a número, ignoramos este libro
                        }

                        // Ignorar si no hay información válida
                        if (shelf.isEmpty() || section.isEmpty() || number == 0) {
                            continue;
                        }

                        // Si no existe la estantería, crearla
                        locationBooks.putIfAbsent(shelf, new LocationDTO(shelf, new ArrayList<>()));

                        // Obtener la sección y agregar el libro a la sección
                        LocationDTO shelfGroup = locationBooks.get(shelf);
                        List<SectionDTO> sections = shelfGroup.sections();
                        Optional<SectionDTO> sectionOpt = sections.stream()
                                .filter(s -> s.section().equals(section))
                                .findFirst();

                        SectionDTO sectionGroup;
                        if (sectionOpt.isPresent()) {
                            sectionGroup = sectionOpt.get();
                        } else {
                            sectionGroup = new SectionDTO(section, new ArrayList<>());
                            sections.add(sectionGroup);
                        }

                        sectionGroup.books().add(new BookLocationDTO(book.getId(), book.getIsbn(), book.getTitle(),
                                book.getAuthor(), book.getPublisher(), book.getPublicationYear(),
                                book.getLocation(), number));
                    }
                }
            }
        }

        // Ordenar las estanterías, secciones y libros
        List<LocationDTO> sortedLocationBooks = new ArrayList<>(locationBooks.values());
        sortedLocationBooks.sort(Comparator.comparing(LocationDTO::shelf));

        for (LocationDTO shelfGroup : sortedLocationBooks) {
            shelfGroup.sections().sort(Comparator.comparing(SectionDTO::section));

            for (SectionDTO sectionGroup : shelfGroup.sections()) {
                sectionGroup.books().sort(Comparator.comparingInt(BookLocationDTO::number));
            }
        }

        return sortedLocationBooks;
    }


    private Book findBookByIsbn(Long isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new AppException("Book not found.", HttpStatus.NOT_FOUND));
    }
    private void existsByIsbn(Long isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new AppException("Un libro con ese ISBN ya existe.", HttpStatus.CONFLICT);
        }
    }
    private void existsByLocation(String location) {
        if (bookRepository.existsByLocation(location)) {
            throw new AppException("Un libro ya tiene esa ubicación.", HttpStatus.CONFLICT);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (!"jpg".equals(extension)) {
                throw new AppException("Solo se permiten archivos con extensión .jpg", HttpStatus.BAD_REQUEST);
            }

            return extension;
        }
        return "";
    }

    public static String validateAndTrim(String input, String defaultValue) {
        if (input != null && !input.trim().isEmpty()) {
            return input.trim().toUpperCase();
        }
        return defaultValue;
    }

    private BookDTO toBookDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getLocation()
        );
    }
}
