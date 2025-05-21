package com.microservico.pedidoservice.infrastructure.controller;

import com.microservico.pedidoservice.application.service.PedidoService;
import com.microservico.pedidoservice.domain.model.Pedido;
import com.microservico.pedidoservice.domain.model.dto.PedidoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public Pedido criarPedido(@RequestBody PedidoRequestDTO pedidoRequestDTO) {
        return pedidoService.processarPedido(pedidoRequestDTO);
    }

    @GetMapping("/{id}")
    public Optional<Pedido> buscarPorId(@PathVariable String id) {  // Corrigido de Long para String
        return pedidoService.porId(id);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarPedidos();
    }

    @PutMapping("/{id}")
    public Pedido atualizarPedido(@PathVariable String id, @RequestBody Pedido pedidoAtualizado) {  // Corrigido de Long para String
        return pedidoService.atualizarPedido(id, pedidoAtualizado);
    }

    @GetMapping("/sku/{sku}")
    public Optional<Pedido> buscarPorSku(@PathVariable String sku) {
        return pedidoService.porSku(sku);
    }
}
