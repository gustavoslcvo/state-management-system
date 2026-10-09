
package com.projeto.sistema.produto;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {
    List<Produto> findAllByOrderByNomeAsc();
}

