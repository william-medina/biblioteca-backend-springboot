package com.williammedina.biblioteca.domain.user.service;

import com.williammedina.biblioteca.domain.user.dto.LoginUserDTO;
import com.williammedina.biblioteca.domain.user.dto.UserDTO;

public interface UserService {

    String authenticateAndGenerateToken(LoginUserDTO data);
    UserDTO getCurrentUser();

}
