package com.microservico.pedidoservice.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservico.pedidoservice.domain.model.Pedido;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
    List<Pedido> findByCpfCliente(String cpfCliente);
    List<Pedido> findByStatus(String status);
    @Query("{ 'itens.sku' : ?0 }")
    List<Pedido> findBySku(String sku);
}