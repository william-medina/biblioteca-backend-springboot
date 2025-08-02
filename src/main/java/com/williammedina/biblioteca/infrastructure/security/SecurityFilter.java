package com.williammedina.biblioteca.infrastructure.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.williammedina.biblioteca.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private static final List<PublicEndpoint> PUBLIC_ENDPOINTS = SecurityConfigurations.PUBLIC_ENDPOINTS;

    private boolean isPublicUrl(String requestUri, String requestMethod) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(endpoint -> {
            String uriPattern = endpoint.url()
                    .replace("{isbn}", "[^/]+")
                    .replace("{keyword}", "[^/]+")
                    .replace("{sortBy}", "[^/]+")
                    .replace("{count}", "[^/]+")
                    .replace("{filename}", "[^/]+")
                    .replace("**", ".*");

            return requestUri.matches(uriPattern) && requestMethod.equals(endpoint.method().name());
        });
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        if (isPublicUrl(requestUri, requestMethod)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el token del header
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");

            try{
                var email = tokenService.getSubjectFromToken(token);
                if (email != null) {
                    // Token valido
                    var user = userRepository.findByEmail(email);
                    var authentication = new UsernamePasswordAuthenticationToken(user, null,
                            user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException e) {
                log.warn("Token verification error: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                String jsonErrorResponse = "{\"error\": \"Unauthorized\"}";
                response.getWriter().write(jsonErrorResponse);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
