package com.example.cadastroclientes.services;

import com.example.cadastroclientes.DTO.AtendimentoRequestDTO;
import com.example.cadastroclientes.DTO.AtendimentoResponseDTO;
import com.example.cadastroclientes.exceptions.AcessoNegadoException;
import com.example.cadastroclientes.exceptions.RecursoNaoEncontradoException;
import com.example.cadastroclientes.model.Admin;
import com.example.cadastroclientes.model.Atendimento;
import com.example.cadastroclientes.model.Cliente;
import com.example.cadastroclientes.repositories.AtendimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ClienteService clienteService;

    public AtendimentoService(AtendimentoRepository atendimentoRepository, ClienteService clienteService) {
        this.atendimentoRepository = atendimentoRepository;
        this.clienteService = clienteService;
    }

    public List<AtendimentoResponseDTO> listarPorCliente(Long clienteId, Admin admin) {
        clienteService.buscarValidando(clienteId, admin); // valida acesso
        return atendimentoRepository.findByClienteIdOrderByDataDesc(clienteId).stream()
                .map(AtendimentoResponseDTO::new).toList();
    }

    public AtendimentoResponseDTO criar(AtendimentoRequestDTO dto, Admin admin) {
        Cliente cliente = clienteService.buscarValidando(dto.clienteId(), admin);
        Atendimento at = new Atendimento();
        at.setData(dto.data());
        at.setDescricao(dto.descricao());
        at.setValor(dto.valor());
        at.setCliente(cliente);
        atendimentoRepository.save(at);
        return new AtendimentoResponseDTO(at);
    }

    public void deletar(Long id, Admin admin) {
        Atendimento at = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado."));
        if (!at.getCliente().getEmpresa().getId().equals(admin.getEmpresa().getId())) {
            throw new AcessoNegadoException("Você não tem permissão para acessar este atendimento.");
        }
        atendimentoRepository.delete(at);
    }
}
