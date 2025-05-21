package com.microservico.pedidoservice.domain.repository;

import com.microservico.pedidoservice.domain.model.ItemPedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ItemPedidoRepository extends MongoRepository<ItemPedido, String> {
    // Buscar um item pelo seu ID
    Optional<ItemPedido> findById(String id);
    
    // Outras consultas customizadas podem ser adicionadas aqui, se necessário
}
