package com.example.khazaana.main;

public class AssetEntry {
    public String stock;
    public float price;
    public float quantity;

    public AssetEntry() {}

    public AssetEntry(String stock, float price, float quantity) {
        this.stock = stock;
        this.price = price;
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getStock() {
        return stock;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
