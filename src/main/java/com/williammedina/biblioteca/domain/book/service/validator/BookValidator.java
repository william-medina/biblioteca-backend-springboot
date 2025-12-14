package com.williammedina.biblioteca.domain.book.service.validator;

public interface BookValidator {

    void ensureIsbnIsUnique(Long isbn);
    void ensureLocationIsAvailable(String location);

}
