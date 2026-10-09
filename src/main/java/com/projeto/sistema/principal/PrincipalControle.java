package com.projeto.sistema.principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrincipalControle {

    @GetMapping({"/", "/administrativo"})
    public ModelAndView accessHome() {
        return new ModelAndView("administrativo/home");
    }
}