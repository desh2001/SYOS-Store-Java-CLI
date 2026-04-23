package com.syos.observer;

/**
 * Observer Pattern — Concrete observer that alerts when shelf stock
 * drops below a critical level (5 units) or reaches zero.
 */
public class LowStockObserver implements StockObserver {
    private static final int CRITICAL_THRESHOLD = 5;

    @Override
    public void onStockChanged(int itemId, String itemName, int newQuantity) {
        if (newQuantity <= 0) {
            System.out.println("\n 🚨 [CRITICAL] " + itemName + 
                " is OUT OF STOCK on shelf! Immediate reshelving required.");
        } else if (newQuantity <= CRITICAL_THRESHOLD) {
            System.out.println("\n 🔴 [LOW STOCK] " + itemName + 
                " shelf stock critically low: only " + newQuantity + " units left!");
        }
    }
}
