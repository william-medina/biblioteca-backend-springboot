package com.williammedina.biblioteca.domain.book;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long isbn;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String author = "S.A";

    @Column(length = 50, nullable = false)
    private String publisher = "S.E";

    @Column(name = "publication_year", length = 6, nullable = false)
    private String publicationYear = "S.F";

    @Column(length = 6, nullable = false)
    private String location = "---";
}
