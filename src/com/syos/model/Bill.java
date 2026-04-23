package com.syos.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * DTO representing a complete bill/transaction.
 * Replaces loose parameter passing (billType, totalAmount, cashTendered, etc.)
 */
public class Bill {
    private int billId;
    private String billType;       // "OFFLINE" or "ONLINE"
    private double totalAmount;
    private double cashTendered;
    private double changeAmount;
    private Date billDate;
    private List<BillItem> items;

    public Bill(String billType) {
        this.billType = billType;
        this.items = new ArrayList<>();
    }

    // Add item to the bill
    public void addItem(BillItem item) {
        this.items.add(item);
        recalculateTotal();
    }

    // Recalculate total from all items
    private void recalculateTotal() {
        this.totalAmount = 0;
        for (BillItem item : items) {
            this.totalAmount += item.getTotal();
        }
    }

    // Process payment
    public void processPayment(double cashTendered) {
        this.cashTendered = cashTendered;
        this.changeAmount = cashTendered - totalAmount;
    }

    // Getters
    public int getBillId() { return billId; }
    public String getBillType() { return billType; }
    public double getTotalAmount() { return totalAmount; }
    public double getCashTendered() { return cashTendered; }
    public double getChangeAmount() { return changeAmount; }
    public Date getBillDate() { return billDate; }
    public List<BillItem> getItems() { return items; }

    // Setters (used by DAO after persistence)
    public void setBillId(int billId) { this.billId = billId; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setCashTendered(double cashTendered) { this.cashTendered = cashTendered; }
    public void setChangeAmount(double changeAmount) { this.changeAmount = changeAmount; }

    // Check if payment is sufficient
    public boolean isPaymentSufficient() {
        return cashTendered >= totalAmount;
    }

    // Check if bill has items
    public boolean hasItems() {
        return !items.isEmpty();
    }
}
