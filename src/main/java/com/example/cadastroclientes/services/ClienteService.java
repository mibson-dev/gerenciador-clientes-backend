package com.example.cadastroclientes.services;

import com.example.cadastroclientes.DTO.*;
import com.example.cadastroclientes.exceptions.AcessoNegadoException;
import com.example.cadastroclientes.exceptions.RecursoNaoEncontradoException;
import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.model.Cliente;
import com.example.cadastroclientes.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponseDTO> listar(Admin admin, String busca) {
        Long empresaId = admin.getEmpresa().getId();
        List<Cliente> clientes = (busca == null || busca.isBlank())
                ? clienteRepository.findByEmpresaIdOrderByNomeAsc(empresaId)
                : clienteRepository.buscar(empresaId, busca.trim());
        return clientes.stream().map(ClienteResponseDTO::new).toList();
    }

    public ClienteResponseDTO criar(ClienteRequestDTO dto, Admin admin) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
        cliente.setDataNascimento(dto.dataNascimento());
        cliente.setObservacoes(dto.observacoes());
        cliente.setEmpresa(admin.getEmpresa());
        clienteRepository.save(cliente);
        return new ClienteResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto, Admin admin) {
        Cliente cliente = buscarValidando(id, admin);
        cliente.setNome(dto.nome());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
        cliente.setDataNascimento(dto.dataNascimento());
        cliente.setObservacoes(dto.observacoes());
        clienteRepository.save(cliente);
        return new ClienteResponseDTO(cliente);
    }

    public void deletar(Long id, Admin admin) {
        Cliente cliente = buscarValidando(id, admin);
        clienteRepository.delete(cliente);
    }

    public DashboardDTO dashboard(Admin admin) {
        Long empresaId = admin.getEmpresa().getId();
        long total = clienteRepository.countByEmpresaId(empresaId);
        int mes = LocalDate.now().getMonthValue();
        List<ClienteResponseDTO> aniversariantes = clienteRepository
                .aniversariantesDoMes(empresaId, mes).stream()
                .map(ClienteResponseDTO::new).toList();
        return new DashboardDTO(total, mes, aniversariantes);
    }

    // Valida que o cliente pertence à empresa do admin (isolamento multi-tenant)
    public Cliente buscarValidando(Long id, Admin admin) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        if (!cliente.getEmpresa().getId().equals(admin.getEmpresa().getId())) {
            throw new AcessoNegadoException("Você não tem permissão para acessar este cliente.");
        }
        return cliente;
    }
}
