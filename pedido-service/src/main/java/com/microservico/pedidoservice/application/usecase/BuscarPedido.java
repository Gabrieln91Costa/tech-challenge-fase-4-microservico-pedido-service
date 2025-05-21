package com.microservico.pedidoservice.application.usecase;

import com.microservico.pedidoservice.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface BuscarPedido {
    Optional<Pedido> porId(String id); // <- ALTERADO DE Long para String
    Optional<Pedido> porSku(String sku);
    List<Pedido> listarPedidos();
}
