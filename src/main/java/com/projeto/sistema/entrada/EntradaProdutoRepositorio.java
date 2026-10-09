package com.projeto.sistema.entrada;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaProdutoRepositorio extends JpaRepository<EntradaProduto, Long> {
    List<EntradaProduto> findAllByOrderByDataEntradaDescIdDesc();
}