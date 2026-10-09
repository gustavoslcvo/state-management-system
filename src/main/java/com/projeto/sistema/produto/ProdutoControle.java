
package com.projeto.sistema.produto;


import com.projeto.sistema.fornecedor.FornecedorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
public class ProdutoControle {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private FornecedorRepositorio fornecedorRepositorio;

    @GetMapping("/cadastroProduto")
    public ModelAndView cadastrar(Produto produto) {
        ModelAndView mv = new ModelAndView("administrativo/produto/cadastro");
        mv.addObject("produto", produto);
        mv.addObject("fornecedores", fornecedorRepositorio.findAllByOrderByNomeAsc());
        return mv;
    }

    @GetMapping("/editarProduto/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        if (produto.isEmpty()) {
            return listar();
        }
        return cadastrar(produto.get());
    }

    @PostMapping("/salvarProduto")
    public ModelAndView salvar(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(produto);
        }
        if (produto.getQuantidadeEstoque() == null) {
            produto.setQuantidadeEstoque(0);
        }
        if (produto.getFornecedorId() != null) {
            fornecedorRepositorio.findById(produto.getFornecedorId()).ifPresentOrElse(
                    produto::setFornecedor,
                    () -> produto.setFornecedor(null)
            );
        } else {
            produto.setFornecedor(null);
        }
        produtoRepositorio.saveAndFlush(produto);
        return new ModelAndView("redirect:/listaProdutos");
    }

    @GetMapping("/listaProdutos")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/produto/lista");
        mv.addObject("produtos", produtoRepositorio.findAllByOrderByNomeAsc());
        return mv;
    }

    @GetMapping("/removerProduto/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        produto.ifPresent(produtoRepositorio::delete);
        return listar();
    }
}
