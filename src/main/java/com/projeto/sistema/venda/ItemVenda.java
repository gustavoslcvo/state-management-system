package com.projeto.sistema.venda;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import com.projeto.sistema.produto.Produto;

import jakarta.persistence.*;

@Entity
@Table(name = "item_venda")
public class ItemVenda implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    private BigDecimal subtotal = BigDecimal.ZERO;

    @Transient
    private Long produtoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Long getProdutoId() {
        return produtoId != null ? produtoId : (produto != null ? produto.getId() : null);
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}
