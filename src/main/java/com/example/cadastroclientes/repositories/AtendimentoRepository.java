package com.example.cadastroclientes.repositories;

import com.example.cadastroclientes.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    List<Atendimento> findByClienteIdOrderByDataDesc(Long clienteId);
}
