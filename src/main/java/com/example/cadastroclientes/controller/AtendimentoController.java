package com.example.cadastroclientes.controller;

import com.example.cadastroclientes.DTO.AtendimentoRequestDTO;
import com.example.cadastroclientes.DTO.AtendimentoResponseDTO;
import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.services.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {
    private final AtendimentoService atendimentoService;
    public AtendimentoController(AtendimentoService atendimentoService) { this.atendimentoService = atendimentoService; }

    @GetMapping
    public ResponseEntity<List<AtendimentoResponseDTO>> listarPorCliente(
            @RequestParam Long clienteId,
            @AuthenticationPrincipal Admin admin) {
        return ResponseEntity.ok(atendimentoService.listarPorCliente(clienteId, admin));
    }

    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> criar(
            @RequestBody @Valid AtendimentoRequestDTO dto,
            @AuthenticationPrincipal Admin admin) {
        return ResponseEntity.status(201).body(atendimentoService.criar(dto, admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal Admin admin) {
        atendimentoService.deletar(id, admin);
        return ResponseEntity.noContent().build();
    }
}
