package com.projeto.sistema.funcionario;

import com.projeto.sistema.estado.EstadoRepositorio;
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
public class FuncionarioControle {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroFuncionario")
    public ModelAndView cadastrar(Funcionario funcionario) {
        ModelAndView mv = new ModelAndView("administrativo/funcionario/cadastro");
        mv.addObject("funcionario", funcionario);
        mv.addObject("estados", estadoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarFuncionario/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioRepositorio.findById(id);
        if (funcionario.isEmpty()) {
            return listar();
        }
        return cadastrar(funcionario.get());
    }

    @PostMapping("/salvarFuncionario")
    public ModelAndView salvar(@Valid Funcionario funcionario, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(funcionario);
        }
        if (funcionario.getEstadoId() != null) {
            estadoRepositorio.findById(funcionario.getEstadoId()).ifPresentOrElse(
                    funcionario::setEstado,
                    () -> funcionario.setEstado(null)
            );
        } else {
            funcionario.setEstado(null);
        }
        funcionarioRepositorio.saveAndFlush(funcionario);
        return new ModelAndView("redirect:/listaFuncionarios");
    }

    @GetMapping("/listaFuncionarios")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/funcionario/lista");
        mv.addObject("funcionarios", funcionarioRepositorio.findAllByOrderByNomeAsc());
        return mv;
    }

    @GetMapping("/removerFuncionario/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioRepositorio.findById(id);
        funcionario.ifPresent(funcionarioRepositorio::delete);
        return listar();
    }
}