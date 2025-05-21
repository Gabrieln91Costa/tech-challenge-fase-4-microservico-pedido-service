package com.microservico.pedidoservice.domain.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTO usado para requisições de criação ou atualização de um pedido.
 */
public class PedidoRequestDTO {

    @NotEmpty(message = "O CPF do cliente é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpfCliente;

    @NotEmpty(message = "O número do cartão é obrigatório.")
    @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter exatamente 16 dígitos numéricos.")
    private String numeroCartao;

    // @NotEmpty(message = "O status do pedido é obrigatório.")
    // private String status;

    @NotNull(message = "O valor total do pedido é obrigatório.")
    @Positive(message = "O valor total deve ser maior que zero.")
    private Double valorTotal;

    @NotEmpty(message = "A lista de itens do pedido não pode estar vazia.")
    private List<@NotNull(message = "Item não pode ser nulo") ItemRequestDTO> itens;

    // Construtores
    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(String cpfCliente, String numeroCartao, String status, Double valorTotal, List<ItemRequestDTO> itens) {
        this.cpfCliente = cpfCliente;
        this.numeroCartao = numeroCartao;
        // this.status = "";
        this.valorTotal = valorTotal;
        this.itens = itens;
    }

    // Getters e Setters
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

    // public String getStatus() {
    //     return status;
    // }

    // public void setStatus(String status) {
    //     this.status = status;
    // }

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
