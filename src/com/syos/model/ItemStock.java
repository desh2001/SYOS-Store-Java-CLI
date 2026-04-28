package com.syos.model;

public class ItemStock {
    private int itemId;
    private String itemCode;
    private String itemName;
    private double unitPrice;
    private int shelfQuantity;

    public ItemStock(int itemId, String itemCode, String itemName, double unitPrice, int shelfQuantity) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.shelfQuantity = shelfQuantity;
    }

    public int getItemId() { return itemId; }
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public double getUnitPrice() { return unitPrice; }
    public int getShelfQuantity() { return shelfQuantity; }

    public boolean isAvailable() {
        return shelfQuantity > 0;
    }
}


