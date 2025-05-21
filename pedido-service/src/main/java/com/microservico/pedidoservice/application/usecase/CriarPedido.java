package com.microservico.pedidoservice.application.usecase;

import com.microservico.pedidoservice.domain.model.Pedido;

public interface CriarPedido {
    Pedido criarPedido(Pedido pedido);
}
