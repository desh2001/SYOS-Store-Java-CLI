package com.syos.observer;

/**
 * Observer Pattern — Interface for objects that need to be notified
 * when stock levels change (after billing or reshelving).
 */
public interface StockObserver {
    /**
     * Called when shelf stock quantity changes for an item.
     * @param itemId   The ID of the item
     * @param itemName The name of the item
     * @param newQuantity The new shelf stock quantity after the change
     */
    void onStockChanged(int itemId, String itemName, int newQuantity);
}
