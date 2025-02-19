package com.williammedina.biblioteca.infrastructure.security;

import com.williammedina.biblioteca.domain.user.User;
import com.williammedina.biblioteca.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {

    private final UserRepository userRepository;

    public AutenticacionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = (User) userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("El usuario con el nombre de usuario " + username + " no fue encontrado.");
        }
        return user;
    }

}
