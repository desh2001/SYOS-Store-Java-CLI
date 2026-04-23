package com.syos.decorator;

/**
 * Decorator Pattern — Applies a percentage discount to the bill total.
 * Wraps another BillComponent and reduces the total by the given percentage.
 */
public class DiscountDecorator extends BillDecorator {
    private double discountPercentage;

    /**
     * @param wrappedBill       The bill component to decorate
     * @param discountPercentage The discount percentage (e.g., 10.0 for 10% off)
     */
    public DiscountDecorator(BillComponent wrappedBill, double discountPercentage) {
        super(wrappedBill);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculateTotal() {
        double baseTotal = wrappedBill.calculateTotal();
        return baseTotal - (baseTotal * discountPercentage / 100.0);
    }

    @Override
    public String getDescription() {
        return wrappedBill.getDescription() + 
               String.format(" → Discount (%.1f%%)", discountPercentage);
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getDiscountAmount() {
        return wrappedBill.calculateTotal() * discountPercentage / 100.0;
    }
}
