package com.example.cadastroclientes.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ClienteRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres") String nome,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres") String telefone,

        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres") String email,

        @Past(message = "A data de nascimento deve ser no passado") LocalDate dataNascimento,

        @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres") String observacoes
) {}