package com.projeto.sistema.fornecedor;

import com.projeto.sistema.estado.EstadoRepositorio;
import com.projeto.sistema.fornecedor.FornecedorRepositorio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class FornecedorControle {

    @Autowired
    private FornecedorRepositorio fornecedorRepositorio;

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroFornecedor")
    public ModelAndView cadastrar(Fornecedor fornecedor) {
        ModelAndView mv = new ModelAndView("administrativo/fornecedor/cadastro");
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("estados", estadoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarFornecedor/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Fornecedor> fornecedor = fornecedorRepositorio.findById(id);
        if (fornecedor.isEmpty()) {
            return listar();
        }
        return cadastrar(fornecedor.get());
    }

    @PostMapping("/salvarFornecedor")
    public ModelAndView salvar(@Valid Fornecedor fornecedor, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(fornecedor);
        }
        if (fornecedor.getEstadoId() != null) {
            estadoRepositorio.findById(fornecedor.getEstadoId()).ifPresentOrElse(
                    fornecedor::setEstado,
                    () -> fornecedor.setEstado(null)
            );
        } else {
            fornecedor.setEstado(null);
        }
        fornecedorRepositorio.saveAndFlush(fornecedor);
        return new ModelAndView("redirect:/listaFornecedores");
    }

    @GetMapping("/listaFornecedores")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/fornecedor/lista");
        mv.addObject("fornecedores", fornecedorRepositorio.findAllByOrderByNomeAsc());
        return mv;
    }

    @GetMapping("/removerFornecedor/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Fornecedor> fornecedor = fornecedorRepositorio.findById(id);
        fornecedor.ifPresent(fornecedorRepositorio::delete);
        return listar();
    }
}
