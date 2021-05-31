package com.example.myapplication;

public class OrderProduct {
    private String id_order;
    private String id_product;
    private int amount;

    public OrderProduct(String id_order, String id_product, int amount) {
        this.id_order = id_order;
        this.id_product = id_product;
        this.amount = amount;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
