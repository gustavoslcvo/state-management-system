
package com.projeto.sistema.fornecedor;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepositorio extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findAllByOrderByNomeAsc();
}