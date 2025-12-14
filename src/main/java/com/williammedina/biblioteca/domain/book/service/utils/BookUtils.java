package com.williammedina.biblioteca.domain.book.service.utils;

public class BookUtils {

    private BookUtils() {}

    public static String normalizeOrDefault(String input, String defaultValue) {
        if (input != null && !input.trim().isEmpty()) {
            return input.trim().toUpperCase();
        }
        return defaultValue;
    }
}
