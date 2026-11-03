package com.example.cadastroclientes.repositories;

import com.example.cadastroclientes.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
