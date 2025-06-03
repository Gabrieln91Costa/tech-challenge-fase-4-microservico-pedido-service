package com.microservico.pedidoservice.application.service;

import com.microservico.pedidoservice.application.usecase.AtualizarPedido;
import com.microservico.pedidoservice.application.usecase.BuscarPedido;
import com.microservico.pedidoservice.application.usecase.CriarPedido;
import com.microservico.pedidoservice.domain.dto.*;
import com.microservico.pedidoservice.domain.model.ItemPedido;
import com.microservico.pedidoservice.domain.model.Pedido;
import com.microservico.pedidoservice.domain.model.StatusPedido;
import com.microservico.pedidoservice.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService implements CriarPedido, BuscarPedido, AtualizarPedido {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String estoqueServiceUrl = "http://estoqueservice:8080/estoque";
    private final String pagamentoServiceUrl = "http://pagamentoservice:8080/pagamentos";

    @Override
    public Pedido criarPedido(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            ResponseEntity<Void> estoqueResponse = restTemplate.getForEntity(
                    estoqueServiceUrl + "/" + item.getSku(), Void.class
            );

            if (estoqueResponse.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("Produto com SKU " + item.getSku() + " não encontrado no estoque.");
            }
        }

        pedido.setStatus(StatusPedido.ABERTO);

        for (ItemPedido item : pedido.getItens()) {
            String urlBaixar = estoqueServiceUrl + "/" + item.getSku() + "/baixar?quantidade=" + item.getQuantidade();
            restTemplate.put(urlBaixar, null);
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido criarEProcessarPagamento(PedidoRequestDTO pedidoRequestDTO) {
        // 1. Cria o pedido
        Pedido pedido = new Pedido();
        pedido.setCpfCliente(pedidoRequestDTO.getCpfCliente());
        pedido.setNumeroCartao(pedidoRequestDTO.getNumeroCartao());
        pedido.setValorTotal(pedidoRequestDTO.getValorTotal());

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemRequestDTO itemDTO : pedidoRequestDTO.getItens()) {
            ItemPedido item = new ItemPedido();
            item.setSku(itemDTO.getSku());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itens.add(item);
        }

        pedido.setItens(itens);
        pedido.setStatus(StatusPedido.ABERTO);
        pedido = pedidoRepository.save(pedido);

        // 2. Prepara DTO de pagamento
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setCpfCliente(pedido.getCpfCliente());
        pagamentoDTO.setNumeroCartao(pedido.getNumeroCartao());
        pagamentoDTO.setValorTotal(pedido.getValorTotal());

        List<ItemPagamentoDTO> itensPagamento = pedido.getItens().stream().map(item -> {
            ItemPagamentoDTO dto = new ItemPagamentoDTO();
            dto.setSku(item.getSku());
            dto.setQuantidade(item.getQuantidade());
            return dto;
        }).collect(Collectors.toList());

        pagamentoDTO.setItens(itensPagamento);

        // 3. Envia solicitação de pagamento
        try {
            ResponseEntity<String> pagamentoResponse = restTemplate.postForEntity(
                    pagamentoServiceUrl,
                    pagamentoDTO,
                    String.class
            );

            if (pagamentoResponse.getStatusCode().is2xxSuccessful()) {
                // Pagamento OK - baixa estoque
                for (ItemPedido item : pedido.getItens()) {
                    ResponseEntity<Void> estoqueResponse = restTemplate.getForEntity(
                            estoqueServiceUrl + "/" + item.getSku(), Void.class
                    );

                    if (estoqueResponse.getStatusCode().is4xxClientError()) {
                        throw new RuntimeException("Produto com SKU " + item.getSku() + " não encontrado no estoque.");
                    }
                }

                for (ItemPedido item : pedido.getItens()) {
                    String urlBaixar = estoqueServiceUrl + "/" + item.getSku() + "/baixar?quantidade=" + item.getQuantidade();
                    restTemplate.put(urlBaixar, null);
                }

                pedido.setStatus(StatusPedido.FECHADO_COM_SUCESSO); // ✅ Ajuste: pedido fechado após sucesso
            } else {
                pedido.setStatus(StatusPedido.FECHADO_SEM_CREDITO); // ❌ Pagamento recusado
            }

        } catch (Exception e) {
            pedido.setStatus(StatusPedido.FECHADO_SEM_CREDITO); // ❌ Erro no pagamento
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido estornarPedido(String id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.FECHADO_SEM_ESTOQUE
                || pedido.getStatus() == StatusPedido.FECHADO_SEM_CREDITO) {

            for (ItemPedido item : pedido.getItens()) {
                String urlRepor = estoqueServiceUrl + "/" + item.getSku() + "/repor";
                restTemplate.put(urlRepor, null);
            }
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> porId(String id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> porSku(String sku) {
        return pedidoRepository.findBySku(sku);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido atualizarPedido(String id, PedidoRequestDTO pedidoRequestDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedidoExistente.setCpfCliente(pedidoRequestDTO.getCpfCliente());
        pedidoExistente.setNumeroCartao(pedidoRequestDTO.getNumeroCartao());
        pedidoExistente.setValorTotal(pedidoRequestDTO.getValorTotal());

        List<ItemPedido> itensAtualizados = new ArrayList<>();
        for (ItemRequestDTO itemDTO : pedidoRequestDTO.getItens()) {
            ItemPedido item = new ItemPedido();
            item.setSku(itemDTO.getSku());
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itensAtualizados.add(item);
        }
        pedidoExistente.setItens(itensAtualizados);

        return pedidoRepository.save(pedidoExistente);
    }
}
