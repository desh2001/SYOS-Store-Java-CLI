package com.syos.observer;

/**
 * Observer Pattern — Concrete observer that alerts when shelf stock
 * drops below the reshelving threshold (20 units).
 */
public class ReorderLevelObserver implements StockObserver {
    private static final int RESHELVE_THRESHOLD = 20;

    @Override
    public void onStockChanged(int itemId, String itemName, int newQuantity) {
        if (newQuantity < RESHELVE_THRESHOLD && newQuantity > 0) {
            System.out.println("\n ⚠ [RESHELVE ALERT] " + itemName + 
                " shelf stock is low: " + newQuantity + " units remaining. Consider reshelving.");
        }
    }
}
