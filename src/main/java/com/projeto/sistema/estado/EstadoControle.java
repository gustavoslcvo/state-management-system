package com.projeto.sistema.estado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class EstadoControle {

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroEstado")
    public ModelAndView cadastrar(Estado estado) {
        ModelAndView mv = new ModelAndView("administrativo/estado/cadastro");
        mv.addObject("estado", estado);
        return mv;
    }

    @GetMapping("/editarEstado/{id}")
    public ModelAndView editar(@PathVariable("id") Long id, Estado estado) {
        Optional<Estado> estadoEncontrado = estadoRepositorio.findById(id);
        if (estadoEncontrado.isEmpty()) {
            return listar();
        }
            return cadastrar(estadoEncontrado.get());
    }

    @PostMapping("/salvarEstado")
    public ModelAndView salvar(Estado estado, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(estado);
        }
        estadoRepositorio.save(estado);
        return new ModelAndView("redirect:/listaEstados");
    }

    @GetMapping({"/listaEstado", "/listaEstados", "/administrativo/estados"})
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/estado/lista");
        mv.addObject("estados", estadoRepositorio.findAll());
        return mv;
    }
    @GetMapping("/removerEstado/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Estado> estado = estadoRepositorio.findById(id);

        estado.ifPresent(estadoRepositorio::delete);

        return listar();
    }
    public EstadoRepositorio getEstadoRepositorio() {
        return estadoRepositorio;
    }
}