package com.microservico.pedidoservice.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class PagamentoDTO {

    @NotEmpty(message = "O CPF do cliente é obrigatório.")
    private String cpfCliente;

    @NotEmpty(message = "O número do cartão é obrigatório.")
    private String numeroCartao;

    @NotNull(message = "O valor total é obrigatório.")
    @Positive(message = "O valor total deve ser maior que zero.")
    private Double valorTotal;

    @NotNull(message = "A lista de itens não pode ser nula.")
    @NotEmpty(message = "A lista de itens não pode estar vazia.")
    private List<ItemPagamentoDTO> itens;

    public PagamentoDTO() {
    }

    public PagamentoDTO(String cpfCliente, String numeroCartao, Double valorTotal, List<ItemPagamentoDTO> itens) {
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

    public List<ItemPagamentoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPagamentoDTO> itens) {
        this.itens = itens;
    }
}
