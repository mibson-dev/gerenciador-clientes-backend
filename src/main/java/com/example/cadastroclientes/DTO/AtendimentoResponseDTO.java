package com.example.cadastroclientes.DTO;

import com.example.cadastroclientes.model.Atendimento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AtendimentoResponseDTO(
        Long id,
        LocalDate data,
        String descricao,
        BigDecimal valor,
        Long clienteId
) {
    public AtendimentoResponseDTO(Atendimento a) {
        this(a.getId(), a.getData(), a.getDescricao(), a.getValor(), a.getCliente().getId());
    }
}
