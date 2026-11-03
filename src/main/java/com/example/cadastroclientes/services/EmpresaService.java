package com.example.cadastroclientes.services;

import com.example.cadastroclientes.DTO.EmpresaRequestDTO;
import com.example.cadastroclientes.exceptions.AcessoNegadoException;
import com.example.cadastroclientes.exceptions.RegraDeNegocioException;
import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.model.Empresa;
import com.example.cadastroclientes.repositories.AdminRepository;
import com.example.cadastroclientes.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.chave-mestra}")
    private String chaveMestraConfigurada;

    public EmpresaService(EmpresaRepository empresaRepository, AdminRepository adminRepository,
                          PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Empresa criar(EmpresaRequestDTO dto, String chaveMestraRecebida) {
        validarChaveMestra(chaveMestraRecebida);

        String email = dto.adminEmail().trim().toLowerCase();
        if (adminRepository.findByEmail(email).isPresent()) {
            throw new RegraDeNegocioException("Já existe um admin com esse e-mail.");
        }

        Empresa empresa = new Empresa();
        empresa.setNome(dto.nomeEmpresa().trim());
        empresa.setTelefone(dto.telefoneEmpresa());
        empresaRepository.save(empresa);

        Admin admin = new Admin();
        admin.setNome(dto.adminNome().trim());
        admin.setEmail(email);
        admin.setSenha(passwordEncoder.encode(dto.adminSenha()));
        admin.setEmpresa(empresa);
        adminRepository.save(admin);

        return empresa;
    }

    private void validarChaveMestra(String recebida) {
        if (chaveMestraConfigurada == null || chaveMestraConfigurada.isBlank()) {
            throw new AcessoNegadoException("Cadastro de empresas desativado.");
        }
        if (recebida == null || !MessageDigest.isEqual(
                chaveMestraConfigurada.getBytes(StandardCharsets.UTF_8),
                recebida.getBytes(StandardCharsets.UTF_8))) {
            throw new AcessoNegadoException("Chave mestra inválida.");
        }
    }
}