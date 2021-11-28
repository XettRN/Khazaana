package com.example.khazaana.main;

public class StockEntry {
    private String symbol;
    private float shares;
    private float price;

    public StockEntry() { }

    public StockEntry(String symbol, float shares, float price) {
        this.symbol = symbol;
        this.shares = shares;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public Number getShares() {
        return shares;
    }

    public Number getPrice() {
        return price;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setShares(float shares) {
        this.shares = shares;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
