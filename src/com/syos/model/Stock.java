package com.syos.model;
import java.util.Date;

public class Stock {
    private int stockId;
    private int itemId;
    private String batchNo;
    private int quantity;
    private Date expiryDate;

    public Stock(int itemId, String batchNo, int quantity, Date expiryDate) {
        this.itemId = itemId;
        this.batchNo = batchNo;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    // Getters
    public int getItemId() { return itemId; }
    public String getBatchNo() { return batchNo; }
    public int getQuantity() { return quantity; }
    public Date getExpiryDate() { return expiryDate; }
}