package com.syos.decorator;

import com.syos.model.Bill;

/**
 * Decorator Pattern — Concrete component wrapping a Bill model.
 * Returns the raw subtotal from all bill items.
 */
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
