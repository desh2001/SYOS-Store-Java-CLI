package com.syos.model;

/**
 * DTO representing a single line item on a bill/order.
 * Used in both POS billing and Online Store ordering.
 */
public class BillItem {
    private int itemId;
    private String itemCode;
    private String name;
    private int quantity;
    private double unitPrice;
    private double total;

    public BillItem(int itemId, String itemCode, String name, int quantity, double unitPrice) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = quantity * unitPrice;
    }

    // Getters
    public int getItemId() { return itemId; }
    public String getItemCode() { return itemCode; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }
}