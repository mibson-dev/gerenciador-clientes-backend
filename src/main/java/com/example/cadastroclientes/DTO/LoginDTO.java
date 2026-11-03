package com.example.cadastroclientes.DTO;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "Informe o e-mail") String email,
        @NotBlank(message = "Informe a senha") String senha
) {}