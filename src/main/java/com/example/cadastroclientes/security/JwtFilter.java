package com.example.cadastroclientes.security;

import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.repositories.AdminRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;

    public JwtFilter(TokenService tokenService, AdminRepository adminRepository) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String email = tokenService.validarToken(token);
            if (email != null) {
                Optional<Admin> adminOpt = adminRepository.findByEmail(email);
                if (adminOpt.isPresent()) {
                    Admin admin = adminOpt.get();
                    var auth = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
