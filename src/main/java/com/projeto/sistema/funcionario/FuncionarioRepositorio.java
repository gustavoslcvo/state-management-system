

package com.projeto.sistema.funcionario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepositorio extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findAllByOrderByNomeAsc();
}