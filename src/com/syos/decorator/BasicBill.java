package com.syos.decorator;

import com.syos.model.Bill;

public class BasicBill implements BillComponent {
    private Bill bill;

    public BasicBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public double calculateTotal() {
        return bill.getTotalAmount();
    }

    @Override
    public String getDescription() {
        return "Subtotal";
    }

    public Bill getBill() {
        return bill;
    }
}


