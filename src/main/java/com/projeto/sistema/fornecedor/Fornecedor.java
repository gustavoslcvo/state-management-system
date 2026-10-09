
package com.projeto.sistema.fornecedor;

import java.io.Serial;
import java.io.Serializable;

import com.projeto.sistema.estado.Estado;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Informe o nome ou razao social")
    private String nome;

    @Column(length = 18)
    private String cnpj;

    private String telefone;

    @Email(message = "Informe um e-mail valido")
    private String email;

    private String contato;

    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;


    @Transient
    private Long estadoId;

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getEstadoId() {
        return estadoId != null ? estadoId : (estado != null ? estado.getId() : null);
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }
}