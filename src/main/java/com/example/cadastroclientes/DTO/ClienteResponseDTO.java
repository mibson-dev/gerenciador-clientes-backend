package com.example.cadastroclientes.DTO;

import com.example.cadastroclientes.model.Cliente;
import java.time.LocalDate;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String telefone,
        String email,
        LocalDate dataNascimento,
        String observacoes
) {
    public ClienteResponseDTO(Cliente c) {
        this(c.getId(), c.getNome(), c.getTelefone(), c.getEmail(), c.getDataNascimento(), c.getObservacoes());
    }
}
