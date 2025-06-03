package com.microservico.pedidoservice.infrastructure.messaging;

import com.microservico.pedidoservice.application.service.PedidoService;
import com.microservico.pedidoservice.domain.dto.PedidoRequestDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {

    private final PedidoService pedidoService;

    public PedidoConsumer(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = "fila.pedidos")
    public void receberPedido(PedidoRequestDTO pedidoRequestDTO) {
        System.out.println("⚙️ ETAPA 06 - INICIANDO PROCESSAMENTO DO PEDIDO RECEBIDO: " + pedidoRequestDTO);

        try {
            // Aqui chamamos o método que já cria o pedido, processa o pagamento
            // e só baixa o estoque se o pagamento for aprovado
            pedidoService.criarEProcessarPagamento(pedidoRequestDTO);

            System.out.println("✅ Pedido criado, pagamento processado e estoque atualizado (se pago) com sucesso!");

        } catch (Exception e) {
            System.out.println("❌ Erro no processamento do pedido: " + e.getMessage());
            // Poderia implementar lógica de retry, dead letter queue, ou compensações aqui
        }
    }
}
