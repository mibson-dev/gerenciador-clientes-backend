package com.example.cadastroclientes.controller;

import com.example.cadastroclientes.DTO.*;
import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;
    public ClienteController(ClienteService clienteService) { this.clienteService = clienteService; }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar(
            @AuthenticationPrincipal Admin admin,
            @RequestParam(required = false) String busca) {
        return ResponseEntity.ok(clienteService.listar(admin, busca));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(
            @RequestBody @Valid ClienteRequestDTO dto,
            @AuthenticationPrincipal Admin admin) {
        return ResponseEntity.status(201).body(clienteService.criar(dto, admin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO dto,
            @AuthenticationPrincipal Admin admin) {
        return ResponseEntity.ok(clienteService.atualizar(id, dto, admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal Admin admin) {
        clienteService.deletar(id, admin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> dashboard(@AuthenticationPrincipal Admin admin) {
        return ResponseEntity.ok(clienteService.dashboard(admin));
    }
}
