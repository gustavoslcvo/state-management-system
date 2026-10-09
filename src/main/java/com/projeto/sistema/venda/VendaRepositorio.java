package com.projeto.sistema.venda;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {
    List<Venda> findAllByOrderByDataVendaDescIdDesc();
}
