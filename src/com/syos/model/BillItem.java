package com.syos.model;

public class BillItem {
    private int itemId;
    private String name;
    private int quantity;
    private double unitPrice;
    private double total;

    public BillItem(int itemId, String name, int quantity, double unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = quantity * unitPrice;
    }

    // Getters (TableView එකට මේවා අනිවාර්යයි)
    public int getItemId() { return itemId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }
}