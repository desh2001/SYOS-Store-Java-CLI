package com.syos.decorator;

/**
 * Decorator Pattern — Applies a tax percentage to the bill total.
 * Wraps another BillComponent and increases the total by the given tax rate.
 */
public class TaxDecorator extends BillDecorator {
    private double taxPercentage;

    /**
     * @param wrappedBill   The bill component to decorate
     * @param taxPercentage The tax percentage (e.g., 8.0 for 8% tax)
     */
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
