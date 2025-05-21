package com.microservico.pedidoservice.domain.model.dto;


public class ItemRequestDTO {

    private String sku;
    private int quantidade;
    private double precoUnitario; // <-- Adicione isso

    public ItemRequestDTO() {
    }

    public ItemRequestDTO(String sku, int quantidade, double precoUnitario) {
        this.sku = sku;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario; // <-- Inicialize aqui
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() { // <-- Getter
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) { // <-- Setter
        this.precoUnitario = precoUnitario;
    }
}
