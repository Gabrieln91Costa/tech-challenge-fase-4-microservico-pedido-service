package com.microservico.pedidoservice.application.usecase;

import com.microservico.pedidoservice.domain.model.Pedido;

public interface AtualizarPedido {
    Pedido atualizarPedido(String id, Pedido pedidoAtualizado);
}
