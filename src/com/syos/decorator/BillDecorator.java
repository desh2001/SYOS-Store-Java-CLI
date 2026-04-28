package com.syos.decorator;

public abstract class BillDecorator implements BillComponent {
    protected BillComponent wrappedBill;

    public BillDecorator(BillComponent wrappedBill) {
        this.wrappedBill = wrappedBill;
    }

    @Override
    public double calculateTotal() {
        return wrappedBill.calculateTotal();
    }

    @Override
    public String getDescription() {
        return wrappedBill.getDescription();
    }
}


