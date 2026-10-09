package com.projeto.sistema.entrada;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.projeto.sistema.fornecedor.Fornecedor;
import com.projeto.sistema.produto.Produto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "entrada_produto")
public class EntradaProduto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @NotNull(message = "Informe a quantidade")
    private Integer quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @NotNull(message = "Informe a data de entrada")
    @Column(name = "data_entrada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEntrada = LocalDate.now();

    private String observacao;

    @Transient
    @NotNull(message = "Selecione um produto")
    private Long produtoId;


    @Transient
    private Long fornecedorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getProdutoId() {
        return produtoId != null ? produtoId : (produto != null ? produto.getId() : null);
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getFornecedorId() {
        return fornecedorId != null ? fornecedorId : (fornecedor != null ? fornecedor.getId() : null);
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }
}