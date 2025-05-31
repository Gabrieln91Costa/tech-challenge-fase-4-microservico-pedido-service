package com.microservico.pedidoservice.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class PedidoRequestDTO {

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

    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(String cpfCliente, String numeroCartao, Double valorTotal, List<ItemRequestDTO> itens) {
        this.cpfCliente = cpfCliente;
        this.numeroCartao = numeroCartao;
        this.valorTotal = valorTotal;
        this.itens = itens;
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
