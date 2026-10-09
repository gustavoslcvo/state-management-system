package com.projeto.sistema.funcionario;

import java.io.Serial;
import java.io.Serializable;

import com.projeto.sistema.estado.Estado;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe o CPF")
    @Column(length = 14)
    private String cpf;

    private String telefone;

    @Email(message = "Informe um e-mail valido")
    private String email;

    @NotBlank(message = "Informe a funcao do funcionario")
    private String funcao;


    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado Estado;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
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
        return Estado;
    }

    public void setEstado(Estado estado) {
        this.Estado = estado;
    }

    public Long getEstadoId() {
        return estadoId != null ? estadoId : (Estado != null ? Estado.getId() : null);
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }
}