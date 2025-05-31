package com.microservico.pedidoservice.infrastructure.messaging;

import com.microservico.pedidoservice.domain.model.Pedido;
import com.microservico.pedidoservice.application.usecase.CriarPedido;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {

    private final CriarPedido criarPedido;

    public PedidoConsumer(CriarPedido criarPedido) {
        this.criarPedido = criarPedido;
    }

    @RabbitListener(queues = "fila.pedidos") // mesma fila usada pelo pedidoreceiver
    public void receberPedido(Pedido pedido) {
        System.out.println("📩 Pedido recebido do RabbitMQ no PedidoService:");
        System.out.println(pedido);

        // Processar o pedido (salvar, verificar estoque, etc.)
        criarPedido.criarPedido(pedido);
    }
}
