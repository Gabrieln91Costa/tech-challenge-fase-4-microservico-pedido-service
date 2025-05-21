package com.microservico.pedidoservice.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservico.pedidoservice.domain.model.Pedido;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {  // Alterando de Long para String para MongoDB

    // Corrigido para buscar por cpfCliente, que é o campo correto na entidade Pedido
    List<Pedido> findByCpfCliente(String cpfCliente);

    // Consulta para buscar pedidos pelo status
    List<Pedido> findByStatus(String status);

    // Consulta personalizada para encontrar pedidos com um item que tem o SKU especificado
    @Query("{ 'itens.sku' : ?0 }")  // Ajustando a consulta para MongoDB
    List<Pedido> findBySku(String sku);
}
