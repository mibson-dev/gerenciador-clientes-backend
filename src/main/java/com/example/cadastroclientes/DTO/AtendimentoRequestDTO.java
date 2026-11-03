package com.example.cadastroclientes.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AtendimentoRequestDTO(
        @NotNull(message = "A data é obrigatória")
        @PastOrPresent(message = "A data do atendimento não pode ser no futuro") LocalDate data,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres") String descricao,

        @PositiveOrZero(message = "O valor não pode ser negativo")
        @Digits(integer = 10, fraction = 2, message = "Valor inválido (até 10 dígitos e 2 casas decimais)") BigDecimal valor,

        @NotNull(message = "O cliente é obrigatório") Long clienteId
) {}