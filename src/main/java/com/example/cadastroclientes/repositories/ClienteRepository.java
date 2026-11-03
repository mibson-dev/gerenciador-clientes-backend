package com.example.cadastroclientes.repositories;

import com.example.cadastroclientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByEmpresaIdOrderByNomeAsc(Long empresaId);

    // Busca por nome ou telefone dentro da empresa
    @Query("SELECT c FROM Cliente c WHERE c.empresa.id = :empresaId AND " +
           "(LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR c.telefone LIKE CONCAT('%', :termo, '%')) " +
           "ORDER BY c.nome ASC")
    List<Cliente> buscar(@Param("empresaId") Long empresaId, @Param("termo") String termo);

    long countByEmpresaId(Long empresaId);

    // Aniversariantes do mês (por mês da data de nascimento)
    @Query("SELECT c FROM Cliente c WHERE c.empresa.id = :empresaId AND " +
           "EXTRACT(MONTH FROM c.dataNascimento) = :mes ORDER BY EXTRACT(DAY FROM c.dataNascimento) ASC")
    List<Cliente> aniversariantesDoMes(@Param("empresaId") Long empresaId, @Param("mes") int mes);
}
