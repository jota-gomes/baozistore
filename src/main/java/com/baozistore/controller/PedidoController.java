package com.baozistore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.baozistore.model.Pedido;
import com.baozistore.repository.PedidoRepository;
import com.baozistore.repository.ClienteRepository;
import com.baozistore.repository.ProdutoRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(
            PedidoRepository repository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // Cadastrar pedido
    @PostMapping
    public ResponseEntity<Pedido> cadastrar(@RequestBody Pedido pedido) {

        if (pedido.getClienteId() == null ||
            pedido.getProdutoId() == null ||
            pedido.getQuantidade() == null ||
            pedido.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        if (!clienteRepository.existsById(pedido.getClienteId()) ||
            !produtoRepository.existsById(pedido.getProdutoId())) {
            return ResponseEntity.badRequest().build();
        }

        pedido.setId(null);
        Pedido novoPedido = repository.save(pedido);

        return ResponseEntity.ok(novoPedido);
    }

    // Listar todos os pedidos
    @GetMapping
    public List<Pedido> listar() {
        return repository.findAll();
    }

    // Consultar pedido pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir pedido pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
