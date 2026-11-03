package com.example.cadastroclientes.config;

import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.model.Empresa;
import com.example.cadastroclientes.repositories.AdminRepository;
import com.example.cadastroclientes.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DadosDemoSeeder implements CommandLineRunner {

    private final EmpresaRepository empresaRepo;
    private final AdminRepository adminRepo;
    private final PasswordEncoder encoder;

    @Value("${app.demo.admin-email}")
    private String emailDemo;

    @Value("${app.demo.admin-senha}")
    private String senhaDemo;

    public DadosDemoSeeder(EmpresaRepository empresaRepo, AdminRepository adminRepo, PasswordEncoder encoder) {
        this.empresaRepo = empresaRepo;
        this.adminRepo = adminRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (empresaRepo.count() > 0) return;

        if (senhaDemo == null || senhaDemo.isBlank()) {
            System.out.println("[Seeder] DEMO_ADMIN_SENHA não definida. Nenhum admin de demonstração foi criado.");
            return;
        }
        if (senhaDemo.length() < 8) {
            System.out.println("[Seeder] DEMO_ADMIN_SENHA precisa ter pelo menos 8 caracteres. Admin de demonstração não criado.");
            return;
        }

        Empresa empresa = new Empresa();
        empresa.setNome("Minha Empresa Demo");
        empresa.setTelefone("(75) 90000-0000");
        empresaRepo.save(empresa);

        Admin admin = new Admin();
        admin.setNome("Administrador");
        admin.setEmail(emailDemo.trim().toLowerCase());
        admin.setSenha(encoder.encode(senhaDemo));
        admin.setEmpresa(empresa);
        adminRepo.save(admin);

        // A senha nunca é impressa no log
        System.out.println("[Seeder] Empresa demo criada. Login: " + admin.getEmail());
    }
}