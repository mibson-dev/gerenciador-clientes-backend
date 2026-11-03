package com.example.cadastroclientes.controller;

import com.example.cadastroclientes.DTO.EmpresaRequestDTO;
import com.example.cadastroclientes.model.Empresa;
import com.example.cadastroclientes.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {
    private final EmpresaService empresaService;
    public EmpresaController(EmpresaService empresaService) { this.empresaService = empresaService; }

    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(
            @RequestHeader(value = "X-Chave-Mestra", required = false) String chaveMestra,
            @RequestBody @Valid EmpresaRequestDTO dto) {
        Empresa empresa = empresaService.criar(dto, chaveMestra);
        return ResponseEntity.status(201).body(Map.of(
                "id", empresa.getId(),
                "nome", empresa.getNome()
        ));
    }
}