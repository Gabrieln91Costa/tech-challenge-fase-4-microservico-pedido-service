package com.microservico.pedidoservice.domain.dto;

public class ItemPagamentoDTO {

    private String sku;
    private Integer quantidade;

    public ItemPagamentoDTO() {
    }

    public ItemPagamentoDTO(String sku, Integer quantidade) {
        this.sku = sku;
        this.quantidade = quantidade;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
