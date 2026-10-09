package com.projeto.sistema.entrada;

import com.projeto.sistema.fornecedor.Fornecedor;
import com.projeto.sistema.fornecedor.FornecedorRepositorio;
import com.projeto.sistema.produto.Produto;
import com.projeto.sistema.produto.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
public class EntradaProdutoControle {

    @Autowired
    private EntradaProdutoRepositorio entradaProdutoRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private FornecedorRepositorio fornecedorRepositorio;

    @GetMapping("/cadastroEntrada")
    public ModelAndView cadastrar(EntradaProduto entradaProduto) {
        ModelAndView mv = new ModelAndView("administrativo/entrada/cadastro");
        mv.addObject("entradaProduto", entradaProduto);
        mv.addObject("produtos", produtoRepositorio.findAllByOrderByNomeAsc());
        mv.addObject("fornecedores", fornecedorRepositorio.findAllByOrderByNomeAsc());
        return mv;
    }

    @GetMapping("/editarEntrada/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<EntradaProduto> entrada = entradaProdutoRepositorio.findById(id);
        if (entrada.isEmpty()) {
            return listar();
        }
        return cadastrar(entrada.get());
    }
    @PostMapping("/salvarEntrada")
    @Transactional
    public ModelAndView salvar(@Valid EntradaProduto entradaProduto, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(entradaProduto);
        }

        Optional<Produto> produtoOpt = produtoRepositorio.findById(entradaProduto.getProdutoId());
        if (produtoOpt.isEmpty()) {
            result.rejectValue("produtoId", "produtoId.invalido", "Produto nao encontrado");
            return cadastrar(entradaProduto);
        }
        Produto produtoNovo = produtoOpt.get();

        boolean edicao = entradaProduto.getId() != null;
        if (edicao) {
            Optional<EntradaProduto> antigaOpt = entradaProdutoRepositorio.findById(entradaProduto.getId());
            if (antigaOpt.isPresent()) {
                EntradaProduto antiga = antigaOpt.get();
                if (antiga.getProduto() != null && antiga.getQuantidade() != null) {
                    if (antiga.getProduto().getId().equals(produtoNovo.getId())) {
                        produtoNovo.setQuantidadeEstoque(produtoNovo.getQuantidadeEstoque() - antiga.getQuantidade());
                    } else {
                        Produto produtoAntigo = antiga.getProduto();
                        produtoAntigo.setQuantidadeEstoque(produtoAntigo.getQuantidadeEstoque() - antiga.getQuantidade());
                        produtoRepositorio.save(produtoAntigo);
                    }
                }
            }
        }

        int quantidade = entradaProduto.getQuantidade() != null ? entradaProduto.getQuantidade() : 0;
        produtoNovo.setQuantidadeEstoque(produtoNovo.getQuantidadeEstoque() + quantidade);
        produtoRepositorio.save(produtoNovo);
        entradaProduto.setProduto(produtoNovo);

        if (entradaProduto.getFornecedorId() != null) {
            Optional<Fornecedor> fornecedorOpt = fornecedorRepositorio.findById(entradaProduto.getFornecedorId());
            entradaProduto.setFornecedor(fornecedorOpt.orElse(null));
        } else {
            entradaProduto.setFornecedor(null);
        }

        entradaProdutoRepositorio.saveAndFlush(entradaProduto);
        return new ModelAndView("redirect:/listaEntradas");
    }

    @GetMapping("/listaEntradas")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/entrada/lista");
        mv.addObject("entradas", entradaProdutoRepositorio.findAllByOrderByDataEntradaDescIdDesc());
        return mv;
    }

    /** Remover uma entrada tambem desfaz o efeito dela no estoque do produto. */
    @GetMapping("/removerEntrada/{id}")
    @Transactional
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<EntradaProduto> entradaOpt = entradaProdutoRepositorio.findById(id);
        if (entradaOpt.isPresent()) {
            EntradaProduto entrada = entradaOpt.get();
            if (entrada.getProduto() != null && entrada.getQuantidade() != null) {
                Produto produto = entrada.getProduto();
                produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - entrada.getQuantidade());
                produtoRepositorio.save(produto);
            }
            entradaProdutoRepositorio.delete(entrada);
        }
        return listar();
    }
}