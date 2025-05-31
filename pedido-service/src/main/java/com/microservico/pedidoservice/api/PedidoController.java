package com.microservico.pedidoservice.api;

import com.microservico.pedidoservice.application.service.PedidoService;
import com.microservico.pedidoservice.domain.model.Pedido;
import com.microservico.pedidoservice.domain.dto.PedidoRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@Validated @RequestBody PedidoRequestDTO pedidoRequestDTO) {
        Pedido pedidoCriado = pedidoService.processarPedido(pedidoRequestDTO);
        return ResponseEntity.ok(pedidoCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable String id) {
        return pedidoService.porId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable String id, @Validated @RequestBody PedidoRequestDTO pedidoRequestDTO) {
        Pedido pedidoAtualizado = pedidoService.atualizarPedido(id, pedidoRequestDTO);
        return ResponseEntity.ok(pedidoAtualizado);
    }
}
