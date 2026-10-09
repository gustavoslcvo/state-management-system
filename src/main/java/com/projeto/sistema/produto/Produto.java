
package com.projeto.sistema.produto;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import com.projeto.sistema.fornecedor.Fornecedor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Informe o nome do produto")
    private String nome;

    private String descricao;

    private String categoria;

    private String unidade = "UN";

    @NotNull(message = "Informe o preco de custo")
    @Column(name = "preco_custo")
    private BigDecimal precoCusto = BigDecimal.ZERO;

    @NotNull(message = "Informe o preco de venda")
    @Column(name = "preco_venda")
    private BigDecimal precoVenda = BigDecimal.ZERO;

    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque = 0;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Transient
    private Long fornecedorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Long getFornecedorId() {
        return fornecedorId != null ? fornecedorId : (fornecedor != null ? fornecedor.getId() : null);
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public boolean isEstoqueBaixo() {
        return quantidadeEstoque != null && quantidadeEstoque <= 5;
    }
}