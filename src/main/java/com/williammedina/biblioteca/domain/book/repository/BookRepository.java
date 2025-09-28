package com.williammedina.biblioteca.domain.book.repository;

import com.williammedina.biblioteca.domain.book.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "CAST(b.isbn AS string) = :keyword OR " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.publicationYear) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BookEntity> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT b FROM Book b ORDER BY b.title ASC")
    List<BookEntity> findAllOrderByTitle();

    @Query("SELECT b FROM Book b " +
            "ORDER BY CASE WHEN b.author LIKE '%S.A%' THEN 1 ELSE 0 END, b.author ASC, b.title ASC")
    List<BookEntity> findAllOrderByAuthor();

    @Query("SELECT b FROM Book b " +
            "ORDER BY CASE WHEN b.publisher LIKE '%S.E%' THEN 1 ELSE 0 END, b.publisher ASC, b.title ASC")
    List<BookEntity> findAllOrderByPublisher();

    @Query("SELECT b FROM Book b " +
            "ORDER BY CASE WHEN b.publicationYear LIKE '%S.F%' THEN 1 ELSE 0 END, b.publicationYear DESC, b.title ASC")
    List<BookEntity> findAllOrderByPublicationYear();

    @Query("SELECT b FROM Book b ORDER BY b.id DESC")
    List<BookEntity> findAllOrderById();

    Optional<BookEntity> findByIsbn(Long isbn);

    @Query(value = "SELECT * FROM books ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<BookEntity> findRandomBooks(@Param("count") long count);

    boolean existsByIsbn(Long isbn);

    boolean existsByLocation(String location);

}
