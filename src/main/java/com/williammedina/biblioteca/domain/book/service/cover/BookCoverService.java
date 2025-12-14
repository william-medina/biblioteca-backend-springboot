package com.williammedina.biblioteca.domain.book.service.cover;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BookCoverService {

    void saveCover(MultipartFile cover, Long isbn);
    void deleteCover(Long isbn);
    String getCoverFileName(Long isbn, MultipartFile cover);
    Resource loadCover(String filename);

}
