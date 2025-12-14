package com.williammedina.biblioteca.domain.book.service.finder;

import com.williammedina.biblioteca.domain.book.entity.BookEntity;

public interface BookFinder {

    BookEntity findBookByIsbn(Long isbn);

}
