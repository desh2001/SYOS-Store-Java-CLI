package com.syos.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class Bill {
    private int billId;
    private String billType;
    private double totalAmount;
    private double cashTendered;
    private double changeAmount;
    private Date billDate;
    private List<BillItem> items;

    public Bill(String billType) {
        this.billType = billType;
        this.items = new ArrayList<>();
    }

    public void addItem(BillItem item) {
        this.items.add(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalAmount = 0;
        for (BillItem item : items) {
            this.totalAmount += item.getTotal();
        }
    }

    public void processPayment(double cashTendered) {
        this.cashTendered = cashTendered;
        this.changeAmount = cashTendered - totalAmount;
    }

    public int getBillId() { return billId; }
    public String getBillType() { return billType; }
    public double getTotalAmount() { return totalAmount; }
    public double getCashTendered() { return cashTendered; }
    public double getChangeAmount() { return changeAmount; }
    public Date getBillDate() { return billDate; }
    public List<BillItem> getItems() { return items; }

    public void setBillId(int billId) { this.billId = billId; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setCashTendered(double cashTendered) { this.cashTendered = cashTendered; }
    public void setChangeAmount(double changeAmount) { this.changeAmount = changeAmount; }

    public boolean isPaymentSufficient() {
        return cashTendered >= totalAmount;
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }
}


