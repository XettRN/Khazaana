package com.example.khazaana.main;

import java.util.HashMap;

public class CryptoStorage {

    public static Double[] ethereum = new Double[9];
    public static Double[] dogecoin = new Double[9];
    public static Double[] bitcoin =new Double[9];

    public static void setEthereum(Double[] ethereum) {
        CryptoStorage.ethereum = ethereum;
    }

    public static void setDogecoin(Double[] dogecoin) {
        CryptoStorage.dogecoin = dogecoin;
    }

    public static void setBitcoin(Double[] bitcoin) {
        CryptoStorage.bitcoin = bitcoin;
    }

    public static Double[] getBitcoin() {
        return bitcoin;
    }

    public static Double[] getDogecoin() {
        return dogecoin;
    }

    public static Double[] getEthereum() {
        return ethereum;
    }
}

