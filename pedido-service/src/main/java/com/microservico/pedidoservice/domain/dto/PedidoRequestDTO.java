package com.microservico.pedidoservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservico.pedidoservice.domain.model.StatusPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Garante robustez se o payload mudar no futuro
public class PedidoRequestDTO {

    // 🔧 Campo opcional para suportar mensagens que já vêm com ID
    private String id;

    // 🔧 Campo opcional para suportar mensagens com status do pedido
    private StatusPedido status;

    @NotEmpty(message = "O CPF do cliente é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpfCliente;

    @NotEmpty(message = "O número do cartão é obrigatório.")
    @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter exatamente 16 dígitos numéricos.")
    private String numeroCartao;

    @NotNull(message = "O valor total do pedido é obrigatório.")
    @Positive(message = "O valor total deve ser maior que zero.")
    private Double valorTotal;

    @NotEmpty(message = "A lista de itens do pedido não pode estar vazia.")
    private List<@NotNull(message = "Item não pode ser nulo") ItemRequestDTO> itens;

    // Construtores
    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(String id, StatusPedido status, String cpfCliente, String numeroCartao, Double valorTotal, List<ItemRequestDTO> itens) {
        this.id = id;
        this.status = status;
        this.cpfCliente = cpfCliente;
        this.numeroCartao = numeroCartao;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemRequestDTO> itens) {
        this.itens = itens;
    }
}
