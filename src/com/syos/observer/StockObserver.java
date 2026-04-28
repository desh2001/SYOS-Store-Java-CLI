package com.syos.observer;

public interface StockObserver {

    void onStockChanged(int itemId, String itemName, int newQuantity);
}


