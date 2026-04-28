package com.syos.decorator;

public class TaxDecorator extends BillDecorator {
    private double taxPercentage;

    public TaxDecorator(BillComponent wrappedBill, double taxPercentage) {
        super(wrappedBill);
        this.taxPercentage = taxPercentage;
    }

    @Override
    public double calculateTotal() {
        double baseTotal = wrappedBill.calculateTotal();
        return baseTotal + (baseTotal * taxPercentage / 100.0);
    }

    @Override
    public String getDescription() {
        return wrappedBill.getDescription() +
               String.format(" → Tax (%.1f%%)", taxPercentage);
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public double getTaxAmount() {
        return wrappedBill.calculateTotal() * taxPercentage / 100.0;
    }
}


