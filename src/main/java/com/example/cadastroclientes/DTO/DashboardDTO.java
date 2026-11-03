package com.example.cadastroclientes.DTO;

import java.util.List;

public record DashboardDTO(
        long totalClientes,
        int mesAtual,
        List<ClienteResponseDTO> aniversariantesDoMes
) {}
