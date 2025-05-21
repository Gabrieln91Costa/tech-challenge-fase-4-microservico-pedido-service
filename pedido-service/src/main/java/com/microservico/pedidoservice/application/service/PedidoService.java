package com.microservico.pedidoservice.application.service;

import com.microservico.pedidoservice.application.usecase.CriarPedido;
import com.microservico.pedidoservice.application.usecase.BuscarPedido;
import com.microservico.pedidoservice.application.usecase.AtualizarPedido;
import com.microservico.pedidoservice.domain.model.Pedido;
import com.microservico.pedidoservice.domain.model.ItemPedido;
import com.microservico.pedidoservice.domain.model.StatusPedido;
import com.microservico.pedidoservice.domain.model.dto.ItemRequestDTO;
import com.microservico.pedidoservice.domain.model.dto.PedidoRequestDTO;
import com.microservico.pedidoservice.domain.repository.PedidoRepository;
import com.microservico.pedidoservice.domain.repository.ItemPedidoRepository; // Importando repositório de itens
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService implements CriarPedido, BuscarPedido, AtualizarPedido {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemRepository;  // Repositório para acessar os itens

    @Autowired
    private RestTemplate restTemplate;

    private final String estoqueServiceUrl = "http://estoqueservice:8080/estoque";  // URL do serviço de estoque

    @Override
    public Pedido criarPedido(Pedido pedido) {
        // Verificar disponibilidade no estoque para cada item do pedido
        for (String itemId : pedido.getItensIds()) {
            // Buscar o item pelo ID
            Optional<ItemPedido> item = itemRepository.findById(itemId);
            if (item.isPresent()) {
                ResponseEntity<Void> estoqueResponse = restTemplate.getForEntity(estoqueServiceUrl + "/" + item.get().getSku(), Void.class);
                if (estoqueResponse.getStatusCode().is4xxClientError()) {
                    throw new RuntimeException("Produto com SKU " + item.get().getSku() + " não encontrado no estoque.");
                }
            } else {
                throw new RuntimeException("Item com ID " + itemId + " não encontrado.");
            }
        }

        // Definir o status do pedido como "ABERTO"
        pedido.setStatus(StatusPedido.ABERTO);

        // Baixar estoque para os itens
        for (String itemId : pedido.getItensIds()) {
            Optional<ItemPedido> item = itemRepository.findById(itemId);
            item.ifPresent(i -> {
                String urlBaixarEstoque = estoqueServiceUrl + "/" + i.getSku() + "/baixar?quantidade=" + i.getQuantidade();
                restTemplate.put(urlBaixarEstoque, null);
            });
        }

        // Remove referências circulares e salva o pedido
        pedido.getItensIds().clear(); // Limpa a lista de IDs dos itens antes de salvar
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> porId(String id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido atualizarPedido(String id, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedidoExistente.setStatus(pedidoAtualizado.getStatus());
        return pedidoRepository.save(pedidoExistente);
    }

    @Override
    public Optional<Pedido> porSku(String sku) {
        // Alteração para buscar pedido por SKU
        return pedidoRepository.findBySku(sku).stream().findFirst();
    }

    // Método para processar o pedido a partir de um DTO
    public Pedido processarPedido(PedidoRequestDTO pedidoRequestDTO) {
        Pedido pedido = new Pedido();

        pedido.setCpfCliente(pedidoRequestDTO.getCpfCliente());
        pedido.setNumeroCartao(pedidoRequestDTO.getNumeroCartao());
        pedido.setValorTotal(pedidoRequestDTO.getValorTotal());

        List<String> itensIds = new ArrayList<>();
        for (ItemRequestDTO itemRequestDTO : pedidoRequestDTO.getItens()) {
            // Criar um novo item e adicionar seu ID na lista de itensIds
            ItemPedido item = new ItemPedido();
            item.setSku(itemRequestDTO.getSku());
            item.setQuantidade(itemRequestDTO.getQuantidade());
            item.setPrecoUnitario(itemRequestDTO.getPrecoUnitario());

            // Salvar item e pegar o ID gerado
            ItemPedido savedItem = itemRepository.save(item);
            itensIds.add(savedItem.getId());  // Armazenar o ID do item no pedido
        }

        pedido.setItensIds(itensIds);
        pedido.setStatus(StatusPedido.ABERTO);

        return criarPedido(pedido);
    }

    // Método para estornar um pedido, repondo o estoque
    public Pedido estornarPedido(String id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.FECHADO_SEM_ESTOQUE || pedido.getStatus() == StatusPedido.FECHADO_SEM_CREDITO) {
            for (String itemId : pedido.getItensIds()) {
                Optional<ItemPedido> item = itemRepository.findById(itemId);
                item.ifPresent(i -> {
                    String urlReporEstoque = estoqueServiceUrl + "/" + i.getSku() + "/repor";
                    restTemplate.put(urlReporEstoque, null);
                });
            }
        }

        pedido.setStatus(StatusPedido.FECHADO_SEM_CREDITO);
        return pedidoRepository.save(pedido);
    }
}
