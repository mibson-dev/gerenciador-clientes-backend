package com.example.cadastroclientes.controller;

import com.example.cadastroclientes.DTO.LoginDTO;
import com.example.cadastroclientes.DTO.TokenDTO;
import com.example.cadastroclientes.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}