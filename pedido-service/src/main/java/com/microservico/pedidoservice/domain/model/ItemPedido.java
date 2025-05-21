package com.microservico.pedidoservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "itens_pedido")  // MongoDB usa coleções, não tabelas
public class ItemPedido {

    @Id
    private String id;  // ID como String para MongoDB

    private String sku;  // SKU do item

    private Integer quantidade;  // Quantidade do item no pedido

    private Double precoUnitario;  // Preço unitário do item

    private String pedidoId;  // Referência ao ID do pedido (em vez de @DBRef, usamos o ID como String)
}
