package com.example.cadastroclientes.services;

import com.example.cadastroclientes.DTO.LoginDTO;
import com.example.cadastroclientes.DTO.TokenDTO;
import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public TokenDTO login(LoginDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));
            Admin admin = (Admin) auth.getPrincipal();
            String token = tokenService.gerarToken(admin);
            return new TokenDTO(token, admin.getEmpresa().getNome(), admin.getNome());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }
    }
}
