package com.williammedina.biblioteca.domain.user.service.validator;

public interface UserValidator {

    void checkIfUserExists(String email);

}
