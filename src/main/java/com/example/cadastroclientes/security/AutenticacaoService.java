package com.example.cadastroclientes.security;

import com.example.cadastroclientes.repositories.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AutenticacaoService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase();
        return adminRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException("Admin não encontrado."));
    }
}