package com.microservico.pedidoservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pedidos")  // MongoDB usa coleções, não tabelas
public class Pedido {

    @Id
    private String id;  // ID como String para MongoDB

    private String cpfCliente;  // Cliente associado ao pedido, por CPF (ou outra identificação)

    private String numeroCartao;  // Número do cartão utilizado para o pedido

    private StatusPedido status;  // Status do pedido (ex: PENDENTE, FINALIZADO)

    private Double valorTotal;  // Valor total do pedido

    private List<String> itensIds = new ArrayList<>();  // Lista de IDs dos itens do pedido (em vez de @DBRef)

    // Método que retorna os itens baseados nos IDs
    // Agora, o método getItens será implementado diretamente no PedidoService
}
