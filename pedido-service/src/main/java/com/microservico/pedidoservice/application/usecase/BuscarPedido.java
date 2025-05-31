package com.microservico.pedidoservice.application.usecase;

import com.microservico.pedidoservice.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface BuscarPedido {

    /**
     * Busca um pedido pelo seu ID único.
     */
    Optional<Pedido> porId(String id);

    /**
     * Busca todos os pedidos que contenham um item com o SKU informado.
     */
    List<Pedido> porSku(String sku);

    /**
     * Retorna todos os pedidos existentes.
     */
    List<Pedido> listarPedidos();
}
