package com.example.khazaana.main;

public class CryptoEntry {
    private String currency;
    private float amount;

    public CryptoEntry() {}

    public CryptoEntry(String currency, float amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public float getAmount() {
        return amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
