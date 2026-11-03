package com.example.cadastroclientes.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmpresaRequestDTO(
        @NotBlank(message = "O nome da empresa é obrigatório")
        @Size(max = 120, message = "O nome da empresa deve ter no máximo 120 caracteres") String nomeEmpresa,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres") String telefoneEmpresa,

        @NotBlank(message = "O nome do admin é obrigatório")
        @Size(max = 120, message = "O nome do admin deve ter no máximo 120 caracteres") String adminNome,

        @NotBlank(message = "O e-mail do admin é obrigatório")
        @Email(message = "E-mail do admin inválido")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres") String adminEmail,

        @NotBlank(message = "A senha do admin é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres") String adminSenha
) {}