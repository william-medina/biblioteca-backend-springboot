package com.williammedina.biblioteca.domain.book.service.location;

import com.williammedina.biblioteca.domain.book.dto.LocationDTO;
import com.williammedina.biblioteca.domain.book.entity.BookEntity;

import java.util.List;

public interface BookLocationService {

    List<LocationDTO> organizeBookLocations(List<BookEntity> books);

}
