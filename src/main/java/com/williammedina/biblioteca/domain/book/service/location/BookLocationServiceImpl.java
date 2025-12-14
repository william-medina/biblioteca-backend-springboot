package com.williammedina.biblioteca.domain.book.service.location;

import com.williammedina.biblioteca.domain.book.dto.BookLocationDTO;
import com.williammedina.biblioteca.domain.book.dto.LocationDTO;
import com.williammedina.biblioteca.domain.book.dto.SectionDTO;
import com.williammedina.biblioteca.domain.book.entity.BookEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class BookLocationServiceImpl implements BookLocationService {

    @Override
    public List<LocationDTO> organizeBookLocations(List<BookEntity> books) {
        Map<String, LocationDTO> locationBooks = new HashMap<>();

        for (BookEntity book : books) {
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
                            log.warn("Invalid position number for book ID {}: {}", book.getId(), position);
                            continue; // Si no se puede convertir a número, ignorar el libro
                        }

                        // Ignorar si no hay información válida
                        if (shelf.isEmpty() || section.isEmpty() || number == 0) {
                            log.warn("Incomplete location data for book ID: {}", book.getId());
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
        log.debug("Sorting shelves, sections, and books");
        List<LocationDTO> sortedLocationBooks = new ArrayList<>(locationBooks.values());
        sortedLocationBooks.sort(Comparator.comparing(LocationDTO::shelf));

        for (LocationDTO shelfGroup : sortedLocationBooks) {
            shelfGroup.sections().sort(Comparator.comparing(SectionDTO::section));

            for (SectionDTO sectionGroup : shelfGroup.sections()) {
                sectionGroup.books().sort(Comparator.comparingInt(BookLocationDTO::number));
            }
        }

        log.info("Book location structure assembled successfully");
        return sortedLocationBooks;
    }
}
