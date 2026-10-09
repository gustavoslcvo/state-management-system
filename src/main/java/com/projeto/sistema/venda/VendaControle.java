
import com.projeto.sistema.produto.Produto;
import com.projeto.sistema.funcionario.FuncionarioRepositorio;
import com.projeto.sistema.produto.ProdutoRepositorio;
import com.projeto.sistema.venda.ItemVenda;
import com.projeto.sistema.venda.Venda;
import com.projeto.sistema.venda.VendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class VendaControle {

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @GetMapping("/cadastroVenda")
    public ModelAndView cadastrar(Venda venda) {
        if (venda.getItens() == null) {
            venda.setItens(new java.util.ArrayList<>());
        }
        if (venda.getItens().isEmpty()) {
            venda.getItens().add(new ItemVenda());
        }
        ModelAndView mv = new ModelAndView("administrativo/venda/cadastro");
        mv.addObject("venda", venda);
        mv.addObject("produtos", produtoRepositorio.findAllByOrderByNomeAsc());
        mv.addObject("funcionarios", funcionarioRepositorio.findAllByOrderByNomeAsc());
        return mv;
    }

    @GetMapping("/editarVenda/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Venda> venda = vendaRepositorio.findById(id);
        if (venda.isEmpty()) {
            return listar();
        }
        return cadastrar(venda.get());
    }
    @PostMapping("/salvarVenda")
    @Transactional
    public ModelAndView salvar(Venda venda, BindingResult result) {

        if (venda.getItens() != null) {
            venda.getItens().removeIf(item -> item.getProdutoId() == null);
        }

        if (venda.getDataVenda() == null) {
            result.rejectValue("dataVenda", "dataVenda.obrigatoria", "Informe a data da venda");
        }
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            result.reject("itens.vazio", "Adicione pelo menos um produto a venda");
        } else {
            for (ItemVenda item : venda.getItens()) {
                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    result.reject("quantidade.invalida", "Informe uma quantidade valida (maior que zero) para cada produto");
                    break;
                }
            }
        }
        if (result.hasErrors()) {
            return cadastrar(venda);
        }


        Map<Long, Produto> produtos = new LinkedHashMap<>();
        Map<Long, Integer> disponivel = new HashMap<>();

        for (ItemVenda item : venda.getItens()) {
            Long produtoId = item.getProdutoId();
            if (!produtos.containsKey(produtoId)) {
                Optional<Produto> produtoOpt = produtoRepositorio.findById(produtoId);
                if (produtoOpt.isEmpty()) {
                    result.reject("produto.invalido", "Um dos produtos selecionados nao foi encontrado");
                    return cadastrar(venda);
                }
                Produto produto = produtoOpt.get();
                produtos.put(produtoId, produto);
                disponivel.put(produtoId, produto.getQuantidadeEstoque());
            }
        }

        boolean edicao = venda.getId() != null;
        Venda vendaExistente = null;
        if (edicao) {
            vendaExistente = vendaRepositorio.findById(venda.getId()).orElse(null);
            if (vendaExistente != null) {
                for (ItemVenda itemAntigo : vendaExistente.getItens()) {
                    if (itemAntigo.getProduto() == null || itemAntigo.getQuantidade() == null) {
                        continue;
                    }
                    Long produtoId = itemAntigo.getProduto().getId();
                    produtos.putIfAbsent(produtoId, itemAntigo.getProduto());
                    disponivel.putIfAbsent(produtoId, itemAntigo.getProduto().getQuantidadeEstoque());
                    disponivel.merge(produtoId, itemAntigo.getQuantidade(), Integer::sum);
                }
            }
        }

        Map<Long, Integer> consumo = new HashMap<>();
        for (ItemVenda item : venda.getItens()) {
            consumo.merge(item.getProdutoId(), item.getQuantidade(), Integer::sum);
        }

        for (Map.Entry<Long, Integer> entry : consumo.entrySet()) {
            int disp = disponivel.getOrDefault(entry.getKey(), 0);
            if (entry.getValue() > disp) {
                Produto produto = produtos.get(entry.getKey());
                String nome = produto != null ? produto.getNome() : "produto";
                result.reject("estoque.insuficiente", "Estoque insuficiente para \"" + nome + "\" (disponivel: " + disp + ")");
            }
        }
        if (result.hasErrors()) {
            return cadastrar(venda);
        }

        for (Map.Entry<Long, Integer> entry : disponivel.entrySet()) {
            Produto produto = produtos.get(entry.getKey());
            int consumido = consumo.getOrDefault(entry.getKey(), 0);
            produto.setQuantidadeEstoque(entry.getValue() - consumido);
            produtoRepositorio.save(produto);
        }

        Venda vendaParaSalvar = (edicao && vendaExistente != null) ? vendaExistente : new Venda();
        vendaParaSalvar.setDataVenda(venda.getDataVenda());
        vendaParaSalvar.setCliente(venda.getCliente());
        vendaParaSalvar.setStatus(Venda.StatusVenda.CONCLUIDA);

        if (venda.getFuncionarioId() != null) {
            funcionarioRepositorio.findById(venda.getFuncionarioId()).ifPresent(vendaParaSalvar::setFuncionario);
        } else {
            vendaParaSalvar.setFuncionario(null);
        }

        vendaParaSalvar.getItens().clear();
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda itemForm : venda.getItens()) {
            Produto produto = produtos.get(itemForm.getProdutoId());
            BigDecimal valorUnitario = itemForm.getValorUnitario() != null
                    ? itemForm.getValorUnitario()
                    : produto.getPrecoVenda();
            BigDecimal subtotal = valorUnitario.multiply(BigDecimal.valueOf(itemForm.getQuantidade()));

            ItemVenda novoItem = new ItemVenda();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(itemForm.getQuantidade());
            novoItem.setValorUnitario(valorUnitario);
            novoItem.setSubtotal(subtotal);
            novoItem.setVenda(vendaParaSalvar);

            vendaParaSalvar.getItens().add(novoItem);
            total = total.add(subtotal);
        }
        vendaParaSalvar.setValorTotal(total);

        vendaRepositorio.saveAndFlush(vendaParaSalvar);
        return new ModelAndView("redirect:/listaVendas");
    }

    @GetMapping("/listaVendas")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/venda/lista");
        mv.addObject("vendas", vendaRepositorio.findAllByOrderByDataVendaDescIdDesc());
        return mv;
    }

    @GetMapping("/verVenda/{id}")
    public ModelAndView ver(@PathVariable("id") Long id) {
        Optional<Venda> venda = vendaRepositorio.findById(id);
        if (venda.isEmpty()) {
            return listar();
        }
        ModelAndView mv = new ModelAndView("administrativo/venda/detalhes");
        mv.addObject("venda", venda.get());
        return mv;
    }

    @GetMapping("/removerVenda/{id}")
    @Transactional
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Venda> vendaOpt = vendaRepositorio.findById(id);
        if (vendaOpt.isPresent()) {
            Venda venda = vendaOpt.get();
            for (ItemVenda item : venda.getItens()) {
                if (item.getProduto() != null && item.getQuantidade() != null) {
                    Produto produto = item.getProduto();
                    produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
                    produtoRepositorio.save(produto);
                }
            }
            vendaRepositorio.delete(venda);
        }
        return listar();
    }
}