package com.syos.observer;

import java.util.ArrayList;
import java.util.List;

public class StockSubject {
    private List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(int itemId, String itemName, int newQuantity) {
        for (StockObserver observer : observers) {
            observer.onStockChanged(itemId, itemName, newQuantity);
        }
    }

    public List<StockObserver> getObservers() {
        return observers;
    }
}


